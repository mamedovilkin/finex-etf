package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentHistoryBinding
import io.github.mamedovilkin.finexetf.view.adapter.TransactionRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.viewmodel.HistoryViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentHistoryBinding must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater)

        fetchFonds()

        binding.apply {
            swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary, null))
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = true
                fetchFonds()
            }
        }

        return binding.root
    }

    private fun fetchFonds() {
        val viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class]

        viewModel.fonds.observe(viewLifecycleOwner) { fonds ->
            binding.apply {
                if (fonds.isNotEmpty()) {
                    val sortedFonds = fonds.sortedByDescending { it.datetimePurchase }
                    transactionsRecyclerView.setHasFixedSize(true)
                    transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
                    transactionsRecyclerView.adapter = TransactionRecyclerViewAdapter(sortedFonds)
                    transactionsRecyclerView.visibility = View.VISIBLE
                    placeholderLinearLayout.visibility = View.GONE
                }
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}