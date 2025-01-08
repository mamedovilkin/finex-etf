package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentMyAssetsBinding
import io.github.mamedovilkin.finexetf.model.view.Asset
import io.github.mamedovilkin.finexetf.model.network.finex.ListFund
import io.github.mamedovilkin.finexetf.model.database.Type
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.myassets.AssetRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.view.adapter.fund.OnClickListener
import io.github.mamedovilkin.finexetf.view.fragment.fund.ChooseFundDialogFragment
import io.github.mamedovilkin.finexetf.viewmodel.MyAssetsViewModel

@AndroidEntryPoint
class MyAssetsFragment : Fragment(), OnClickListener {

    private var _binding: FragmentMyAssetsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentMyAssetsBinding must not be null")
    private lateinit var viewModel: MyAssetsViewModel
    private lateinit var funds: List<ListFund>
    private lateinit var assets: List<Asset>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyAssetsBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[MyAssetsViewModel::class]

        viewModel.getExchangeRate().observe(viewLifecycleOwner) { rates ->
            viewModel.getAssets().observe(viewLifecycleOwner) {
                assets = it
                binding.apply {
                    progressBar.hide()
                    if (assets.isNotEmpty()) {
                        assetsRecyclerView.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(context)
                            val assetRecyclerViewAdapter = AssetRecyclerViewAdapter(assets, childFragmentManager, viewLifecycleOwner.lifecycle)
                            assetRecyclerViewAdapter.onClickListener = this@MyAssetsFragment
                            assetRecyclerViewAdapter.rates = rates
                            adapter = assetRecyclerViewAdapter
                            show()
                        }
                        placeholderLinearLayout.hide()
                        addSell.show()
                    } else {
                        assetsRecyclerView.hide()
                        placeholderLinearLayout.show()
                        addSell.hide()
                    }
                }
            }
        }

        viewModel.funds.observe(viewLifecycleOwner) {
            funds = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addPurchase.setOnClickListener {
                if (funds.isNotEmpty()) {
                    ChooseFundDialogFragment(funds, Type.PURCHASE).show(parentFragmentManager, "ChooseFundDialogFragment")
                }
            }

            addSell.setOnClickListener {
                if (assets.isNotEmpty()) {
                    val availableListFunds = mutableListOf<ListFund>()
                    assets.forEach {
                        availableListFunds.add(ListFund(it.ticker, it.icon, it.name, it.originalName))
                    }
                    ChooseFundDialogFragment(availableListFunds, Type.SELL).show(parentFragmentManager, "ChooseFundDialogFragment")
                }
            }
        }
    }

    override fun onFundClickListener(ticker: String) {
        findNavController().navigate(R.id.action_my_assets_to_fund, bundleOf("ticker" to ticker))
    }
}