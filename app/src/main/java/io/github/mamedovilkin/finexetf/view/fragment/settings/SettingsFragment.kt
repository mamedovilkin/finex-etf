package io.github.mamedovilkin.finexetf.view.fragment.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import io.github.mamedovilkin.finexetf.util.getCacheSize
import io.github.mamedovilkin.finexetf.util.formatSize
import io.github.mamedovilkin.finexetf.util.isNetworkAvailable
import java.util.Locale

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel: SettingsViewModel by viewModels()
    private var user: FirebaseUser? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val clearImageCache = findPreference<Preference>("clear_image_cache")
        val deleteAllData = findPreference<Preference>("delete_all_data")
        val feedback = findPreference<Preference>("feedback")
        val aboutFinExETF = findPreference<Preference>("about_finex_etf")
        val aboutDeveloper = findPreference<Preference>("about_developer")
        val version = findPreference<Preference>("version")

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
            Toast.makeText(context, context?.resources?.getString(R.string.all_data_has_been_deleted), Toast.LENGTH_LONG).show()
            true
        }

        feedback?.setOnPreferenceClickListener {
            val uri = Uri.parse("mailto:ilkinmamedovdev@gmail.com?subject=" + context?.resources?.getString(R.string.subject))
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO, uri), context?.resources?.getString(R.string.send_feedback)))
            true
        }

        aboutFinExETF?.setOnPreferenceClickListener {
            if (Locale.getDefault().language == "ru") {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://finex-etf.ru/")))
            } else {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://finexetf.com/")))
            }
            true
        }

        aboutDeveloper?.setOnPreferenceClickListener {
            if (Locale.getDefault().language == "ru") {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mamedovilkin.github.io/ru")))
            } else {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mamedovilkin.github.io/")))
            }
            true
        }

        version?.title = resources.getString(R.string.version, BuildConfig.VERSION_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val account = findPreference<Preference>("account")
        val backupData = findPreference<Preference>("backup_data")
        val language = findPreference<Preference>("language")

        language?.summary = Locale.getDefault().displayName.replaceFirstChar(Char::titlecase)

        val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ -> }

        language?.setOnPreferenceClickListener {
            settingsLauncher.launch(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
            true
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            updatePreference(account, backupData, user)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Log.e("SettingsFragment", error)
            }
        }

        viewModel.getCurrentUser().observe(viewLifecycleOwner) { user ->
            updatePreference(account, backupData, user)
        }

        val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val googleAccount = task.getResult(ApiException::class.java)
                googleAccount?.idToken?.let { viewModel.signInWithGoogle(it) }
            } catch (e: ApiException) {
                Log.e("SettingsFragment", e.message.toString())
            }
        }

        account?.setOnPreferenceClickListener {
            if (isNetworkAvailable(inflater.context)) {
                if (user == null) {
                    signInLauncher.launch(googleSignInClient.signInIntent)
                } else {
                    val alertDialog = AlertDialog.Builder(inflater.context).create()
                    alertDialog.setTitle(resources.getString(R.string.sign_out))
                    alertDialog.setMessage(resources.getString(R.string.sign_out_summary))
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes)) { _, _ ->
                        user = null
                        viewModel.signOut()
                    }
                    alertDialog.show()
                }
            } else {
                Toast.makeText(inflater.context, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
            }

            true
        }

        backupData?.setOnPreferenceClickListener {
            if (isNetworkAvailable(inflater.context)) {
                if (user != null) {
                    viewModel.assets.observe(viewLifecycleOwner) {
                        if (it.isNotEmpty()) {
                            viewModel.backupAssets(user!!.uid, it)
                        } else {
                            viewModel.getBackup(user!!.uid)
                        }
                    }
                }

                Toast.makeText(context, context?.resources?.getString(R.string.synced), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(inflater.context, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
            }
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun updatePreference(account: Preference?, backupData: Preference?, user: FirebaseUser?) {
        if (user != null) {
            this.user = user
            account?.summary = resources.getString(R.string.signed_in, user.displayName)
            backupData?.isEnabled = true
            backupData?.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_backup_enabled_24, null)
        } else {
            account?.summary = resources.getString(R.string.account_summary)
            backupData?.isEnabled = false
            backupData?.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_backup_disabled_24, null)
        }
    }
}