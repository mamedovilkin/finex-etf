package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.databinding.FragmentHistoryBinding
import io.github.mamedovilkin.finexetf.view.adapter.TransactionRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.viewmodel.HistoryViewModel
import okhttp3.internal.filterList

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentHistoryBinding must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater)

        val viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class]

        viewModel.fonds.observe(viewLifecycleOwner) { fonds ->
            if (fonds.isNotEmpty()) {
                binding.apply {
                    val sortedFonds = fonds.sortedByDescending { it.datetimePurchase }
                    transactionsRecyclerView.setHasFixedSize(true)
                    transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
                    transactionsRecyclerView.adapter = TransactionRecyclerViewAdapter(sortedFonds)
                    transactionsRecyclerView.visibility = View.VISIBLE
                    placeholderLinearLayout.visibility = View.GONE
                }
            }

        }

        return binding.root
    }
}