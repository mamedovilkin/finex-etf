package io.github.mamedovilkin.finexetf.view.fragment.card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.mamedovilkin.finexetf.databinding.FragmentNetWorthRubCardBinding

class NetWorthRUBCardFragment : Fragment() {

    private var _binding: FragmentNetWorthRubCardBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentNetWorthRubCardBinding must not be null")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNetWorthRubCardBinding.inflate(inflater)

        return binding.root
    }
}