package io.github.mamedovilkin.finexetf.view.fragment.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.mamedovilkin.finexetf.databinding.FragmentNetWorthUsdCardBinding

class NetWorthUSDCardFragment : Fragment() {

    private var _binding: FragmentNetWorthUsdCardBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentNetWorthUsdCardBinding must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNetWorthUsdCardBinding.inflate(inflater)

        return binding.root
    }
}