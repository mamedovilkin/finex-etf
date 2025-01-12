package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentMyAssetsBinding
import io.github.mamedovilkin.finexetf.model.Asset
import io.github.mamedovilkin.network.model.finex.ListFund
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.isNetworkAvailable
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.myassets.AssetRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.view.adapter.fund.OnClickListener
import io.github.mamedovilkin.finexetf.view.fragment.fund.ChooseFundDialogFragment
import io.github.mamedovilkin.finexetf.viewmodel.myassets.MyAssetsViewModel

@AndroidEntryPoint
class MyAssetsFragment : Fragment(), OnClickListener {

    private var _binding: FragmentMyAssetsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentMyAssetsBinding must not be null")
    private val viewModel: MyAssetsViewModel by viewModels()
    private var funds: List<ListFund> = emptyList()
    private var assets: List<Asset> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyAssetsBinding.inflate(inflater)

        viewModel.funds.observe(viewLifecycleOwner) {
            funds = it
        }

        if (isNetworkAvailable(binding.root.context)) {
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
        } else {
            binding.apply {
                placeholderImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.no_internet_connection, null))
                placeholderTextView.text = resources.getString(R.string.no_internet_connection)
                placeholderLinearLayout.show()
                progressBar.hide()
                addPurchase.hide()
                addSell.hide()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addPurchase.setOnClickListener {
                if (funds.isNotEmpty()) {
                    if (isNetworkAvailable(binding.root.context)) {
                        ChooseFundDialogFragment(funds, Type.PURCHASE).show(
                            parentFragmentManager,
                            "ChooseFundDialogFragment"
                        )
                    } else {
                        Toast.makeText(binding.root.context, resources.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }

            addSell.setOnClickListener {
                if (assets.isNotEmpty() && isNetworkAvailable(binding.root.context)) {
                    val availableListFunds = mutableListOf<ListFund>()
                    assets.forEach {
                        availableListFunds.add(ListFund(it.ticker, it.icon, it.name, it.originalName, it.isActive))
                    }
                    ChooseFundDialogFragment(availableListFunds, Type.SELL).show(parentFragmentManager, "ChooseFundDialogFragment")
                } else {
                    Toast.makeText(binding.root.context, resources.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onFundClickListener(ticker: String) {
        if (isNetworkAvailable(binding.root.context)) {
            findNavController().navigate(R.id.action_my_assets_to_fund, bundleOf("ticker" to ticker))
        } else {
            Toast.makeText(binding.root.context, resources.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        }
    }
}