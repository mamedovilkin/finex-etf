package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentMyAssetsBinding
import io.github.mamedovilkin.finexetf.model.Fonds
import io.github.mamedovilkin.finexetf.view.fragment.dialog.ChooseFondDialogFragment
import io.github.mamedovilkin.finexetf.viewmodel.MyAssetsViewModel

@AndroidEntryPoint
class MyAssetsFragment : Fragment() {

    private var _binding: FragmentMyAssetsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentMyAssetsBinding must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyAssetsBinding.inflate(inflater)

        val viewModel = ViewModelProvider(requireActivity())[MyAssetsViewModel::class]
        var fonds: Fonds? = null

        viewModel.fonds.observe(viewLifecycleOwner) {
            fonds = it
            binding.addSell.visibility = View.VISIBLE
            binding.addPurchase.visibility = View.VISIBLE
        }

        binding.addPurchase.setOnClickListener {
            if (!fonds.isNullOrEmpty()) {
                ChooseFondDialogFragment(fonds ?: Fonds()).show(parentFragmentManager, "FondListDialogFragment")
            }
        }

        binding.addSell.setOnClickListener {
            Toast.makeText(it.context, resources.getString(R.string.you_don_t_have_assets_to_add_sell), Toast.LENGTH_LONG).show()
        }

        return binding.root
    }
}