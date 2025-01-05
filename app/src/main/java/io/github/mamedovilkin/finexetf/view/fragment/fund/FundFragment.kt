package io.github.mamedovilkin.finexetf.view.fragment.fund

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentFundBinding
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.viewmodel.FundViewModel
import java.util.Date

@AndroidEntryPoint
class FundFragment : Fragment() {

    private var _binding: FragmentFundBinding? = null
    private val binding: FragmentFundBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentFundBinding must not be null")
    private lateinit var viewModel: FundViewModel
    private var rate: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFundBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[FundViewModel::class]

        val dateReq = DateFormat.format("dd/MM/yyyy", Date()).toString()

        viewModel.getExchangeRate(dateReq).observe(viewLifecycleOwner) {
            rate = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ticker = arguments?.getString("ticker").toString()

        viewModel.getFund(ticker).observe(viewLifecycleOwner) { fund ->
            binding.apply {
                when (fund.ticker) {
                    "FXTP" -> {
                        Glide
                            .with(root.context)
                            .load(fund.icon)
                            .fitCenter()
                            .into(imageView)
                    }

                    "FXRE" -> {
                        imageView.setImageDrawable(resources.getDrawable(R.drawable.fxre, null))
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
                priceTextView.text = "${String.format("%.2f", (fund.nav.navPerShare * rate))}₽"
                textView.text = fund.text.substringBefore("[!")
                progressBar.hide()
                linearLayout.show()
            }
        }
    }
}