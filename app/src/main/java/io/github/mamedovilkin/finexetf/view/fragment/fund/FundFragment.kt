package io.github.mamedovilkin.finexetf.view.fragment.fund

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentFundBinding
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.viewmodel.fund.FundViewModel

@AndroidEntryPoint
class FundFragment : Fragment() {

    private var _binding: FragmentFundBinding? = null
    private val binding: FragmentFundBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentFundBinding must not be null")
    private val viewModel: FundViewModel by viewModels()
    private var rates: List<Double> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFundBinding.inflate(inflater)

        viewModel.getExchangeRate().observe(viewLifecycleOwner) {
            rates = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ticker = arguments?.getString("ticker").toString()

        (activity as AppCompatActivity).supportActionBar?.title = ticker

        viewModel.getFund(ticker).observe(viewLifecycleOwner) { fund ->
            binding.apply {
                when (fund.ticker) {
                    "FXTP" -> {
                        GlideApp.with(root.context).load(fund.icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imageView)
                    }

                    "FXRE" -> {
                        imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.fxre, null))
                    }

                    else -> {
                        imageView.load(fund.icon) {
                            decoderFactory { result, options, _ ->
                                SvgDecoder(
                                    result.source,
                                    options
                                )
                            }
                        }
                    }
                }

                nameTextView.text = fund.originalName.trim()
                tickerTextView.text = fund.ticker
                when (fund.nav.currencyNav) {
                    "USD" -> {
                        priceTextView.text = "${String.format("%.2f", (fund.nav.navPerShare * rates[0]))}₽"
                    }
                    "EUR" -> {
                        priceTextView.text = "${String.format("%.2f", (fund.nav.navPerShare * rates[1]))}₽"
                    }
                    "KZT" -> {
                        priceTextView.text = "${String.format("%.2f", (fund.nav.navPerShare * rates[2]))}₽"
                    }
                    else -> {
                        priceTextView.text = "${String.format("%.2f", fund.nav.navPerShare)}₽"
                    }
                }
                textView.text = fund.text.substringBefore("[!")
                progressBar.hide()
                scrollView.show()
            }
        }
    }
}