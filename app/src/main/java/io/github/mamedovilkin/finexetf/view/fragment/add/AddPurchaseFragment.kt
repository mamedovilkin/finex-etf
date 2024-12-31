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
import io.github.mamedovilkin.finexetf.databinding.FragmentAddPurchaseBinding
import io.github.mamedovilkin.finexetf.room.Fond
import io.github.mamedovilkin.finexetf.viewmodel.AddPurchaseViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AddPurchaseFragment : Fragment() {

    private var _binding: FragmentAddPurchaseBinding? = null
    private val binding: FragmentAddPurchaseBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentAddPurchaseBinding must not be null")
    private lateinit var viewModel: AddPurchaseViewModel
    private var fond: io.github.mamedovilkin.finexetf.model.Fond? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddPurchaseBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[AddPurchaseViewModel::class]
        fond = BundleCompat.getParcelable(requireArguments(), "fond", io.github.mamedovilkin.finexetf.model.Fond::class.java)

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

                addPurchaseButton.setOnClickListener {
                    val quantityInt = quantityTextInputLayoutEditText.text.toString().toIntOrNull()
                    val datetimePurchaseString = datetimePurchaseTextInputLayoutEditText.text.toString()

                    if (
                        quantityInt != null &&
                        quantityInt != 0 &&
                        datetimePurchaseString.isNotEmpty()
                    ) {
                        val quantity = quantityInt.toInt()
                        try {
                            val datetimePurchase = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse(datetimePurchaseString).time
                            viewModel.insert(Fond(fond!!.ticker, fond!!.icon, fond!!.name, fond!!.originalName, quantity, datetimePurchase))
                            findNavController().popBackStack()
                            Toast.makeText(context,  resources.getText(R.string.purchase_has_been_added), Toast.LENGTH_LONG).show()
                        } catch (e: ParseException) {
                            Toast.makeText(context,  resources.getText(R.string.datetime_type_invalid), Toast.LENGTH_LONG).show()
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