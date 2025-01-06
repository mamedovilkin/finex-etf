package io.github.mamedovilkin.finexetf.view.fragment.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.mamedovilkin.finexetf.databinding.FragmentPostBinding

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentPostBinding must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostBinding.inflate(inflater)

        val url = arguments?.getString("url")

        if (url != null) {
            binding.webView.loadUrl(url)
        } else {
            findNavController().popBackStack()
        }

        return binding.root
    }
}