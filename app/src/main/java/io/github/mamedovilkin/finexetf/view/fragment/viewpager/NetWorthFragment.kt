package io.github.mamedovilkin.finexetf.view.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentNetWorthBinding
import io.github.mamedovilkin.finexetf.model.Currency
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.viewmodel.myassets.NetWorthViewModel

@AndroidEntryPoint
class NetWorthFragment(private val currency: Currency) : Fragment() {

    constructor() : this(Currency.RUB)

    private var _binding: FragmentNetWorthBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentNetWorthBinding must not be null")
    private val viewModel: NetWorthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNetWorthBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNetWorth(currency).observe(viewLifecycleOwner) {
            val netWorth = it[0]
            val change = it[1]
            val percentChange = it[2]

            when (currency) {
                Currency.RUB -> {
                    bindNetWorthRUB(netWorth, change, percentChange)
                }
                Currency.USD -> {
                    bindNetWorthUSD(netWorth, change, percentChange)
                }
                Currency.EUR -> {
                    bindNetWorthEUR(netWorth, change, percentChange)
                }
                Currency.KZT -> {
                    bindNetWorthKZT(netWorth, change, percentChange)
                }
            }

            binding.apply {
                progressBar.hide()
                netWorthTextView.show()
                changeTextView.show()
            }
        }
    }

    private fun bindNetWorthRUB(netWorth: Double, change: Double, percentChange: Double) {
        binding.apply {
            netWorthTextView.text = resources.getString(R.string.price_rub, netWorth)
            if (change > 0.0 && percentChange > 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_green, null)
                changeTextView.text = resources.getString(R.string.positive_change_rub, change, percentChange)
            } else if (change < 0.0 && percentChange < 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_red, null)
                changeTextView.text = resources.getString(R.string.change_rub, change, percentChange)
            } else {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_normal, null)
                changeTextView.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                changeTextView.text = resources.getString(R.string.change_rub, change, percentChange)
            }
        }
    }

    private fun bindNetWorthUSD(netWorth: Double, change: Double, percentChange: Double) {
        binding.apply {
            netWorthTextView.text = resources.getString(R.string.price_usd, netWorth)
            if (change > 0.0 && percentChange > 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_green, null)
                changeTextView.text = resources.getString(R.string.positive_change_usd, change, percentChange)
            } else if (change < 0.0 && percentChange < 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_red, null)
                changeTextView.text = resources.getString(R.string.change_usd, change, percentChange)
            } else {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_normal, null)
                changeTextView.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                changeTextView.text = resources.getString(R.string.change_usd, change, percentChange)
            }
        }
    }

    private fun bindNetWorthEUR(netWorth: Double, change: Double, percentChange: Double) {
        binding.apply {
            netWorthTextView.text = resources.getString(R.string.price_eur, netWorth)
            if (change > 0.0 && percentChange > 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_green, null)
                changeTextView.text = resources.getString(R.string.positive_change_eur, change, percentChange)
            } else if (change < 0.0 && percentChange < 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_red, null)
                changeTextView.text = resources.getString(R.string.change_eur, change, percentChange)
            } else {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_normal, null)
                changeTextView.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                changeTextView.text = resources.getString(R.string.change_eur, change, percentChange)
            }
        }
    }

    private fun bindNetWorthKZT(netWorth: Double, change: Double, percentChange: Double) {
        binding.apply {
            netWorthTextView.text = resources.getString(R.string.price_kzt, netWorth)
            if (change > 0.0 && percentChange > 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_green, null)
                changeTextView.text = resources.getString(R.string.positive_change_kzt, change, percentChange)
            } else if (change < 0.0 && percentChange < 0.0) {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_red, null)
                changeTextView.text = resources.getString(R.string.change_kzt, change, percentChange)
            } else {
                changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_normal, null)
                changeTextView.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                changeTextView.text = resources.getString(R.string.change_kzt, change, percentChange)
            }
        }
    }
}