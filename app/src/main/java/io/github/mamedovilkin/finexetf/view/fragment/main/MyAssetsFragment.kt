package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.databinding.FragmentMyAssetsBinding
import io.github.mamedovilkin.finexetf.model.view.Asset
import io.github.mamedovilkin.finexetf.model.network.ListFund
import io.github.mamedovilkin.finexetf.model.database.Type
import io.github.mamedovilkin.finexetf.model.view.ExchangeRate
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.AssetRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.view.fragment.dialog.ChooseFundDialogFragment
import io.github.mamedovilkin.finexetf.viewmodel.MyAssetsViewModel
import java.util.Date

@AndroidEntryPoint
class MyAssetsFragment : Fragment() {

    private var _binding: FragmentMyAssetsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentMyAssetsBinding must not be null")
    private lateinit var viewModel: MyAssetsViewModel
    private lateinit var exchangeRate: ExchangeRate
    private lateinit var funds: List<ListFund>
    private lateinit var assets: List<Asset>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyAssetsBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[MyAssetsViewModel::class]

        val dateReq = DateFormat.format("dd/MM/yyyy", Date()).toString()

        viewModel.getExchangeRate(dateReq).observe(viewLifecycleOwner) {
            exchangeRate = it
        }

        viewModel.getAssets().observe(viewLifecycleOwner) {
            assets = it
            binding.apply {
                progressBar.hide()
                if (assets.isNotEmpty()) {
                    assetsRecyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        val assetRecyclerViewAdapter = AssetRecyclerViewAdapter(assets, viewModel, childFragmentManager, viewLifecycleOwner.lifecycle)
                        assetRecyclerViewAdapter.rate = exchangeRate.rate
                        assetRecyclerViewAdapter.dateFrom = exchangeRate.dateFrom
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
}