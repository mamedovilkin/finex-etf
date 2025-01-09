package io.github.mamedovilkin.finexetf.view.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentNetWorthKztBinding
import io.github.mamedovilkin.finexetf.model.view.Currency
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.viewmodel.NetWorthViewModel

@AndroidEntryPoint
class NetWorthKZTFragment : Fragment() {

    private var _binding: FragmentNetWorthKztBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentNetWorthKztBinding must not be null")
    private lateinit var viewModel: NetWorthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNetWorthKztBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[NetWorthViewModel::class]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNetWorth(Currency.KZT).observe(viewLifecycleOwner) {
            val netWorth = it[0]
            val change = it[1]
            val percentChange = it[2]
            binding.apply {
                netWorthTextView.text = "${String.format("%.2f", netWorth)}₸"
                if (change > 0.0 && percentChange > 0.0) {
                    changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_green, null)
                    changeTextView.text = "+${String.format("%.2f", change)}₸ (+${String.format("%.2f", percentChange)}%)"
                } else if (change < 0.0 && percentChange < 0.0) {
                    changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_red, null)
                    changeTextView.text = "${String.format("%.2f", change)}₸ (${String.format("%.2f", percentChange)}%)"
                } else {
                    changeTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.shape_change_text_view_normal, null)
                    changeTextView.text = "${String.format("%.2f", change)}₸ (${String.format("%.2f", percentChange)}%)"
                }
                progressBar.hide()
                netWorthTextView.show()
                changeTextView.show()
            }
        }
    }
}