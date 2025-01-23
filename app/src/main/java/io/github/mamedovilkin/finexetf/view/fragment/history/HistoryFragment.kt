package io.github.mamedovilkin.finexetf.view.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentHistoryBinding
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.isNetworkAvailable
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.history.OnClickListener
import io.github.mamedovilkin.finexetf.view.adapter.history.TransactionRecyclerViewAdapter

@AndroidEntryPoint
class HistoryFragment : Fragment(), OnClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentHistoryBinding must not be null")
    private val viewModel: HistoryViewModel by viewModels()
    private var adapter: TransactionRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater)

        if (isNetworkAvailable(binding.root.context)) {
            viewModel.assets.observe(viewLifecycleOwner) { assets ->
                binding.apply {
                    progressBar.hide()
                    if (assets.isNotEmpty()) {
                        val sortedAssets = assets.sortedByDescending { it.datetime }
                        transactionsRecyclerView.setHasFixedSize(true)
                        transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
                        adapter = TransactionRecyclerViewAdapter(sortedAssets)
                        adapter?.onClickListener = this@HistoryFragment
                        transactionsRecyclerView.adapter = adapter
                        transactionsRecyclerView.show()
                        placeholderLinearLayout.hide()
                    } else {
                        transactionsRecyclerView.hide()
                        placeholderLinearLayout.show()
                    }
                }
            }
        } else {
            binding.apply {
                placeholderImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.no_internet_connection, null))
                placeholderTextView.text = resources.getString(R.string.no_internet_connection)
                placeholderLinearLayout.show()
                progressBar.hide()
            }
        }

        return binding.root
    }

    override fun onTransactionClickListener(asset: Asset, position: Int) {
        val alertDialog = AlertDialog.Builder(binding.root.context).create()
        if (Converter.toType(asset.type) == Type.PURCHASE) {
            alertDialog.setTitle(resources.getString(R.string.delete_this_purchase))
        } else {
            alertDialog.setTitle(resources.getString(R.string.delete_this_sell))
        }
        alertDialog.setMessage(resources.getString(R.string.delete_message, asset.ticker, asset.quantity))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete)) { dialog, _ ->
            viewModel.delete(asset)
            adapter?.notifyItemRemoved(position)
            dialog.dismiss()
            Toast.makeText(binding.root.context, resources.getString(R.string.deleted), Toast.LENGTH_LONG).show()
        }
        alertDialog.show()
    }
}