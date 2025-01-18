package io.github.mamedovilkin.finexetf.view.fragment.fund

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentAddBinding
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.viewmodel.fund.AddViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentAddBinding must not be null")
    private lateinit var viewModel: AddViewModel
    private var ticker: String? = null
    private var type: String? = null
    private var totalQuantity: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[AddViewModel::class]
        ticker = arguments?.getString("ticker")
        type = arguments?.getString("type")

        if (type != null || ticker == null) {
            if (Converter.toType(type ?: Converter.fromType(Type.PURCHASE)) == Type.PURCHASE) {
                (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.add_purchase)
            } else {
                (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.add_sell)
            }
        } else {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ticker != null) {
            viewModel.getFund(ticker ?: "FXUS").observe(viewLifecycleOwner) { fund ->
                if (fund != null) {
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
                                    decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                                }
                            }
                        }

                        if (Locale.getDefault().language == "ru") {
                            nameTextView.text = fund.name.trim()
                        } else {
                            nameTextView.text = fund.originalName.trim()
                        }
                        tickerTextView.text = fund.ticker
                        progressBar.hide()
                        scrollView.show()
                        linearLayout.show()

                        if (Converter.toType(type ?: Converter.fromType(Type.PURCHASE)) == Type.PURCHASE) {
                            dateTimeTextInputLayout.hint = resources.getString(R.string.date_time_purchase)
                            priceTextInputLayout.hint = resources.getString(R.string.price_purchase)
                            addButton.text = resources.getString(R.string.add_purchase)
                            addButton.setBackgroundResource(R.drawable.shape_add_purchase_button)
                            splitLinearLayout.show()
                        } else {
                            viewModel.getFundQuantity(ticker ?: "FXUS").observe(viewLifecycleOwner) {
                                totalQuantity = it
                                quantityTextInputLayout.helperText = resources.getString(R.string.you_have, it)
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
                                val id = UUID.randomUUID().toString()
                                val quantity = quantityInt.toString().toLong()
                                val price = priceDouble.toString().toDouble()
                                try {
                                    val datetime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse(datetimeString.toString())?.time
                                    if (datetime != null) {
                                        if (Converter.toType(type ?: Converter.fromType(Type.PURCHASE)) == Type.SELL && quantity > totalQuantity) {
                                            Toast.makeText(context, resources.getString(R.string.you_don_t_have_enough_funds), Toast.LENGTH_LONG).show()
                                        } else {
                                            viewModel.insert(Asset(id, fund.ticker, fund.icon, fund.name, fund.originalName, fund.isActive, quantity, datetime, price, type ?: Converter.fromType(Type.PURCHASE)))
                                            findNavController().popBackStack()

                                            if (Converter.toType(type ?: Converter.fromType(Type.PURCHASE)) == Type.PURCHASE) {
                                                Toast.makeText(context, resources.getText(R.string.purchase_has_been_added), Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(context, resources.getText(R.string.sell_has_been_added), Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                } catch (e: ParseException) {
                                    Toast.makeText(context, resources.getText(R.string.date_time_type_invalid), Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(context, resources.getText(R.string.fields_is_empty), Toast.LENGTH_LONG).show()
                            }
                        }

                        splitLinearLayout.setOnClickListener {
                            if (Locale.getDefault().language == "ru") {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.wikipedia.org/wiki/%D0%94%D1%80%D0%BE%D0%B1%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5_%D0%B0%D0%BA%D1%86%D0%B8%D0%B9")))
                            } else {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Stock_split")))
                            }
                        }
                    }
                } else {
                    findNavController().popBackStack()
                }
            }
        }
    }
}