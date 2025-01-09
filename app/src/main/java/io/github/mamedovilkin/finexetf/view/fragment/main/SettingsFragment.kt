package io.github.mamedovilkin.finexetf.view.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.finexetf.viewmodel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val viewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class]

        findPreference<Preference>("about_finex_etf")?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://finexetf.com/")))
            true
        }

        findPreference<Preference>("about_ilkin_mamedov")?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://mamedovilkin.github.io/")))
            true
        }

        val imageCacheDir = File(context?.cacheDir, "image_manager_disk_cache") // Glide cache path
        val cacheSize = getCacheSize(imageCacheDir)

        findPreference<Preference>("clear_image_cache")?.summary = "${formatSize(cacheSize)} / 100 MB"

        findPreference<Preference>("clear_image_cache")?.setOnPreferenceClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                GlideApp.get(requireActivity()).clearDiskCache()
                withContext(Dispatchers.Main) {
                    findPreference<Preference>("clear_image_cache")?.summary = "0 KB / 100 MB"
                    Toast.makeText(context, context?.resources?.getString(R.string.image_cache_has_been_deleted), Toast.LENGTH_LONG).show()
                }
            }
            true
        }

        findPreference<Preference>("delete_all_data")?.setOnPreferenceClickListener {
            viewModel.deleteAllAssets()
            Toast.makeText(context,  context?.resources?.getString(R.string.data_has_been_deleted), Toast.LENGTH_LONG).show()
            true
        }

        findPreference<Preference>("feedback")?.setOnPreferenceClickListener {
            val uri = Uri.parse("mailto:ilkinmamedov0208@gmail.com?subject=" + context?.resources?.getString(R.string.subject))
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO, uri), context?.resources?.getString(R.string.send_feedback)))
            true
        }
    }

    private fun getCacheSize(cacheDir: File): Long {
        var totalSize = 0L

        fun calculateSize(directory: File) {
            val files = directory.listFiles() ?: return
            for (file in files) {
                if (file.isFile) {
                    totalSize += file.length()
                } else if (file.isDirectory) {
                    calculateSize(file)
                }
            }
        }

        calculateSize(cacheDir)
        return totalSize
    }

    private fun formatSize(size: Long): String {
        val kb = size / 1024
        val mb = kb / 1024
        return if (mb > 0) "$mb MB" else "$kb KB"
    }
}