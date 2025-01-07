package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentHistoryBinding
import io.github.mamedovilkin.finexetf.model.database.Asset
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.history.OnClickListener
import io.github.mamedovilkin.finexetf.view.adapter.history.TransactionRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.viewmodel.HistoryViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment(), OnClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentHistoryBinding must not be null")
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: TransactionRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class]

        viewModel.assets.observe(viewLifecycleOwner) { assets ->
            binding.apply {
                progressBar.hide()
                if (assets.isNotEmpty()) {
                    val sortedAssets = assets.sortedByDescending { it.datetime }
                    transactionsRecyclerView.setHasFixedSize(true)
                    transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
                    adapter = TransactionRecyclerViewAdapter(sortedAssets)
                    adapter.onClickListener = this@HistoryFragment
                    transactionsRecyclerView.adapter = adapter
                    transactionsRecyclerView.show()
                    placeholderLinearLayout.hide()
                } else {
                    transactionsRecyclerView.hide()
                    placeholderLinearLayout.show()
                }
            }
        }

        return binding.root
    }

    override fun onTransactionClickListener(asset: Asset) {
        val alertDialog = AlertDialog.Builder(binding.root.context).create()
        alertDialog.setTitle("Delete this ${asset.type.lowercase()}?")
        alertDialog.setMessage("${asset.ticker} (${asset.quantity} pcs.)")
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel), { dialog, _ -> dialog.dismiss() })
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete), { dialog, _ ->
            viewModel.delete(asset)
            adapter.notifyDataSetChanged()
            dialog.dismiss()
            Toast.makeText(binding.root.context, resources.getString(R.string.deleted), Toast.LENGTH_LONG).show()
        })
        alertDialog.show()
    }
}