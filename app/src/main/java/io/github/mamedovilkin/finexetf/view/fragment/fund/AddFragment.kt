package io.github.mamedovilkin.finexetf.view.fragment.fund

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentAddBinding
import io.github.mamedovilkin.finexetf.database.Converter
import io.github.mamedovilkin.finexetf.model.database.Asset
import io.github.mamedovilkin.finexetf.model.database.Type
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.viewmodel.AddViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentAddBinding must not be null")
    private lateinit var viewModel: AddViewModel
    private lateinit var ticker: String
    private lateinit var type: String
    private var totalQuantity: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[AddViewModel::class]
        ticker = arguments?.getString("ticker").toString()
        type = arguments?.getString("type").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFund(ticker).observe(viewLifecycleOwner) { fund ->
            if (fund != null) {
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
                                decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                            }
                        }
                    }

                    nameTextView.text = fund.originalName.trim()
                    tickerTextView.text = fund.ticker
                    progressBar.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE

                    if (Converter.toType(type) == Type.PURCHASE) {
                        dateTimeTextInputLayout.hint = resources.getString(R.string.date_time_purchase)
                        priceTextInputLayout.hint = resources.getString(R.string.price_purchase)
                        addButton.text = resources.getString(R.string.add_purchase)
                        addButton.setBackgroundResource(R.drawable.shape_add_purchase_button)
                        splitLinearLayout.show()
                    } else {
                        viewModel.getFundQuantity(ticker).observe(viewLifecycleOwner) {
                            totalQuantity = it
                            quantityTextInputLayout.helperText = "You have $it funds"
                        }
                        dateTimeTextInputLayout.hint = resources.getString(R.string.date_time_sell)
                        priceTextInputLayout.hint = resources.getString(R.string.price_sell)
                        addButton.text = resources.getString(R.string.add_sell)
                        addButton.setBackgroundResource(R.drawable.shape_add_sell_button)
                        splitLinearLayout.hide()
                    }

                    addButton.setOnClickListener {
                        val quantityInt = quantityTextInputLayoutEditText.text
                        val datetimeString = dateTimeTextInputLayoutEditText.text
                        val priceDouble = priceTextInputLayoutEditText.text

                        if (
                            quantityInt.toString().toIntOrNull() != null &&
                            datetimeString.toString() != "" &&
                            priceDouble.toString().toDoubleOrNull() != null
                        ) {
                            val quantity = quantityInt.toString().toInt()
                            val price = priceDouble.toString().toDouble()
                            try {
                                val datetime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse(datetimeString.toString()).time
                                if (Converter.toType(type) == Type.SELL && quantity > totalQuantity) {
                                    Toast.makeText(context, resources.getString(R.string.you_don_t_have_enough_funds), Toast.LENGTH_LONG).show()
                                } else {
                                    viewModel.insert(Asset(0, fund.ticker, fund.icon, fund.name, fund.originalName, fund.nav.navPerShare, quantity, datetime, price, type))
                                    findNavController().popBackStack()

                                    if (Converter.toType(type) == Type.PURCHASE) {
                                        Toast.makeText(context, resources.getText(R.string.purchase_has_been_added), Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, resources.getText(R.string.sell_has_been_added), Toast.LENGTH_LONG).show()
                                    }
                                }

                            } catch (e: ParseException) {
                                Toast.makeText(context, resources.getText(R.string.date_time_type_invalid), Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, resources.getText(R.string.fields_is_empty), Toast.LENGTH_LONG).show()
                        }
                    }

                    splitButton.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Stock_split")))
                    }
                }
            } else {
                findNavController().popBackStack()
            }
        }
    }
}