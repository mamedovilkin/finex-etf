package io.github.mamedovilkin.finexetf.view.fragment.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.mamedovilkin.finexetf.databinding.FragmentDollarCardBinding

class DollarCardFragment : Fragment() {

    private var _binding: FragmentDollarCardBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentDollarCardBinding must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDollarCardBinding.inflate(inflater)

        return binding.root
    }
}