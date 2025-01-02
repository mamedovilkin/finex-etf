package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.databinding.FragmentMyAssetsBinding
import io.github.mamedovilkin.finexetf.model.Asset
import io.github.mamedovilkin.finexetf.model.Funds
import io.github.mamedovilkin.finexetf.room.Type
import io.github.mamedovilkin.finexetf.view.adapter.AssetRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.view.fragment.dialog.ChooseFundDialogFragment
import io.github.mamedovilkin.finexetf.viewmodel.MyAssetsViewModel

@AndroidEntryPoint
class MyAssetsFragment : Fragment() {

    private var _binding: FragmentMyAssetsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentMyAssetsBinding must not be null")
    private lateinit var remoteFunds: Funds
    private lateinit var assets: List<Asset>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyAssetsBinding.inflate(inflater)

        val viewModel = ViewModelProvider(requireActivity())[MyAssetsViewModel::class]

        viewModel.remoteFunds.observe(viewLifecycleOwner) {
            remoteFunds = it
            binding.addPurchase.visibility = View.VISIBLE
        }

        viewModel.getAssets().observe(viewLifecycleOwner) {
            assets = it
            binding.apply {
                if (assets.isNotEmpty()) {
                    assetsRecyclerView.setHasFixedSize(true)
                    assetsRecyclerView.layoutManager = LinearLayoutManager(context)
                    assetsRecyclerView.adapter = AssetRecyclerViewAdapter(assets, childFragmentManager, viewLifecycleOwner.lifecycle)
                    assetsRecyclerView.visibility = View.VISIBLE
                    placeholderLinearLayout.visibility = View.GONE
                    addSell.visibility = View.VISIBLE
                } else {
                    assetsRecyclerView.visibility = View.GONE
                    placeholderLinearLayout.visibility = View.VISIBLE
                    addSell.visibility = View.GONE
                }
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addPurchase.setOnClickListener {
                if (!remoteFunds.isEmpty()) {
                    ChooseFundDialogFragment(remoteFunds, Type.PURCHASE).show(parentFragmentManager, "ChooseFundDialogFragment")
                }
            }

            addSell.setOnClickListener {
                if (assets.isNotEmpty()) {
                    val availableListFunds = Funds()
                    assets.forEach {
                        availableListFunds.add(io.github.mamedovilkin.finexetf.model.ListFund(it.ticker, it.icon, it.name, it.originalName))
                    }
                    ChooseFundDialogFragment(availableListFunds, Type.SELL).show(parentFragmentManager, "ChooseFundDialogFragment")
                }
            }
        }
    }
}