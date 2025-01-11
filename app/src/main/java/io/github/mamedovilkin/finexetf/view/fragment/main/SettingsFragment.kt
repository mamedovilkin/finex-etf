package io.github.mamedovilkin.finexetf.view.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.BuildConfig
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.finexetf.viewmodel.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import io.github.mamedovilkin.finexetf.util.getCacheSize
import io.github.mamedovilkin.finexetf.util.formatSize

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel: SettingsViewModel by viewModels()
    private var user: FirebaseUser? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val aboutFinExETF = findPreference<Preference>("about_finex_etf")
        val aboutDeveloper = findPreference<Preference>("about_developer")
        val version = findPreference<Preference>("version")
        val clearImageCache = findPreference<Preference>("clear_image_cache")
        val deleteAllData = findPreference<Preference>("delete_all_data")
        val feedback = findPreference<Preference>("feedback")

        aboutFinExETF?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://finexetf.com/")))
            true
        }

        aboutDeveloper?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mamedovilkin.github.io/")))
            true
        }

        version?.title = "${resources.getString(R.string.version)} ${BuildConfig.VERSION_NAME}"

        val imageCacheDir = File(context?.cacheDir, "image_manager_disk_cache")
        val cacheSize = getCacheSize(imageCacheDir)

        clearImageCache?.summary = "${formatSize(cacheSize)} / 100 MB"

        clearImageCache?.setOnPreferenceClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                GlideApp.get(requireActivity()).clearDiskCache()
                withContext(Dispatchers.Main) {
                    clearImageCache.summary = "0 KB / 100 MB"
                    Toast.makeText(context, context?.resources?.getString(R.string.image_cache_has_been_deleted), Toast.LENGTH_LONG).show()
                }
            }
            true
        }

        deleteAllData?.setOnPreferenceClickListener {
            viewModel.deleteAllAssets()
            Toast.makeText(context,  context?.resources?.getString(R.string.data_has_been_deleted), Toast.LENGTH_LONG).show()
            true
        }

        feedback?.setOnPreferenceClickListener {
            val uri = Uri.parse("mailto:ilkinmamedov0208@gmail.com?subject=" + context?.resources?.getString(R.string.subject))
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO, uri), context?.resources?.getString(R.string.send_feedback)))
            true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val backupData = findPreference<Preference>("backup_data")

        viewModel.getCurrentUser().observe(viewLifecycleOwner) { user ->
            updatePreference(backupData, user)
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            updatePreference(backupData, user)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Log.e("SettingsFragment", error)
            }
        }

        val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { viewModel.signInWithGoogle(it) }
            } catch (e: ApiException) {
                Log.e("SettingsFragment", e.message.toString())
            }
        }

        backupData?.setOnPreferenceClickListener {
            if (user == null) {
                signInLauncher.launch(googleSignInClient.signInIntent)
            } else {
                viewModel.signOut()
            }
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun updatePreference(preference: Preference?, user: FirebaseUser?) {
        if (user != null) {
            this.user = user
            preference?.summary = "Signed in as ${user.displayName}"
        } else {
            preference?.summary = resources.getString(R.string.backup_data_summary)
        }
    }
}