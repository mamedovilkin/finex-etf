package io.github.mamedovilkin.finexetf.view.fragment.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil3.load
import coil3.svg.SvgDecoder
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentAddBinding
import io.github.mamedovilkin.finexetf.room.Converter
import io.github.mamedovilkin.finexetf.room.Fond
import io.github.mamedovilkin.finexetf.room.Type
import io.github.mamedovilkin.finexetf.viewmodel.AddPurchaseViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentAddPurchaseBinding must not be null")
    private lateinit var viewModel: AddPurchaseViewModel
    private var fond: io.github.mamedovilkin.finexetf.model.Fond? = null
    private var type: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[AddPurchaseViewModel::class]
        fond = BundleCompat.getParcelable(requireArguments(), "fond", io.github.mamedovilkin.finexetf.model.Fond::class.java)
        type = arguments?.getString("type")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fond != null) {
            binding.apply {
                when (fond!!.ticker) {
                    "FXTP" -> {
                        Glide
                            .with(root.context)
                            .load(fond!!.icon)
                            .fitCenter()
                            .into(imageView)
                    }
                    "FXRE" -> {
                        imageView.setImageDrawable(resources.getDrawable(R.drawable.fxre, null))
                    }
                    else -> {
                        imageView.load(fond!!.icon) {
                            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                        }
                    }
                }

                nameTextView.text = fond!!.originalName.trim()
                tickerTextView.text = fond!!.ticker
                progressBar.visibility = View.GONE
                linearLayout.visibility = View.VISIBLE

                if (Converter.toType(type ?: "") == Type.PURCHASE) {
                    dateTimeTextInputLayout.hint = resources.getString(R.string.date_time_purchase)
                    priceTextInputLayout.hint = resources.getString(R.string.price_purchase)
                    addButton.text = resources.getString(R.string.add_purchase)
                } else {
                    dateTimeTextInputLayout.hint = resources.getString(R.string.date_time_sell)
                    priceTextInputLayout.hint = resources.getString(R.string.price_sell)
                    addButton.text = resources.getString(R.string.add_sell)
                }

                addButton.setOnClickListener {
                    val quantityInt = quantityTextInputLayoutEditText.text
                    val datetimeString = dateTimeTextInputLayoutEditText.text
                    val priceDouble = priceTextInputLayoutEditText.text

                    if (
                        type != null &&
                        quantityInt.toString().toIntOrNull() != null &&
                        datetimeString.toString() != "" &&
                        priceDouble.toString().toDoubleOrNull() != null
                    ) {
                        val quantity = quantityInt.toString().toInt()
                        val price = priceDouble.toString().toDouble()
                        try {
                            val datetime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse(datetimeString.toString()).time
                            viewModel.insert(Fond(0, fond!!.ticker, fond!!.icon, fond!!.name, fond!!.originalName, quantity, datetime, price, type!!))
                            findNavController().popBackStack()
                            if (Converter.toType(type ?: "") == Type.PURCHASE) {
                                Toast.makeText(context,  resources.getText(R.string.purchase_has_been_added), Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, resources.getText(R.string.sell_has_been_added), Toast.LENGTH_LONG).show()
                            }
                        } catch (e: ParseException) {
                            Toast.makeText(context, resources.getText(R.string.date_time_type_invalid), Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context, resources.getText(R.string.fields_is_empty), Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            findNavController().popBackStack()
        }
    }
}