package io.github.mamedovilkin.finexetf.view.fragment.fund

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentChooseFundDialogBinding
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.network.model.finex.ListFund
import io.github.mamedovilkin.finexetf.view.adapter.fund.ChooseFundRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.view.adapter.fund.OnClickListener

class ChooseFundDialogFragment(private val funds: List<ListFund>, private val type: Type) :
    BottomSheetDialogFragment(),
    OnClickListener,
    SearchView.OnQueryTextListener {

    private var _binding: FragmentChooseFundDialogBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentChooseFundDialogBinding must not be null")

    private var adapter: ChooseFundRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseFundDialogBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            searchView.setOnQueryTextListener(this@ChooseFundDialogFragment)
            fundsRecyclerView.setHasFixedSize(true)
            fundsRecyclerView.layoutManager = LinearLayoutManager(context)
            adapter = ChooseFundRecyclerViewAdapter(funds)
            adapter?.onClickListener = this@ChooseFundDialogFragment
            fundsRecyclerView.adapter = adapter
        }
    }

    override fun onFundClickListener(ticker: String) {
        findNavController().navigate(R.id.action_my_assets_to_add, bundleOf("ticker" to ticker, "type" to Converter.fromType(type)))
        dismiss()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            val filteredFunds = mutableListOf<ListFund>()

            for(fund in funds) {
                if (fund.ticker.lowercase().contains(newText.lowercase())
                    || fund.originalName.lowercase().contains(newText.lowercase())
                    || fund.name.lowercase().contains(newText.lowercase())) {
                    filteredFunds.add(fund)
                }
            }

            if (filteredFunds.isNotEmpty()) {
                adapter?.funds = filteredFunds
                adapter?.notifyDataSetChanged()
            }
        }

        return false
    }
}