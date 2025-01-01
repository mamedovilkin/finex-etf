package io.github.mamedovilkin.finexetf.view.fragment.card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentRubleCardBinding

class RubleCardFragment : Fragment() {

    private var _binding: FragmentRubleCardBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentRubleCardBinding must not be null")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRubleCardBinding.inflate(inflater)

        return binding.root
    }
}