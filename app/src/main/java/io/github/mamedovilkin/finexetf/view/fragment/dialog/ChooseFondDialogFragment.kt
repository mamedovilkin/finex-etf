package io.github.mamedovilkin.finexetf.view.fragment.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import android.widget.SearchView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentChooseFondDialogBinding
import io.github.mamedovilkin.finexetf.model.Fond
import io.github.mamedovilkin.finexetf.model.Fonds
import io.github.mamedovilkin.finexetf.room.Converter
import io.github.mamedovilkin.finexetf.room.Type
import io.github.mamedovilkin.finexetf.view.adapter.ChooseFondRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.view.adapter.OnItemClickListener

class ChooseFondDialogFragment(private val fonds: ArrayList<Fond>, private val type: Type) :
    BottomSheetDialogFragment(),
    OnItemClickListener,
    SearchView.OnQueryTextListener {

    private var _binding: FragmentChooseFondDialogBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentChooseFondDialogBinding must not be null")

    private lateinit var adapter: ChooseFondRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseFondDialogBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            searchView.setOnQueryTextListener(this@ChooseFondDialogFragment)
            fondsRecyclerView.setHasFixedSize(true)
            fondsRecyclerView.layoutManager = LinearLayoutManager(context)
            adapter = ChooseFondRecyclerViewAdapter(fonds)
            adapter.onItemClickListener = this@ChooseFondDialogFragment
            fondsRecyclerView.adapter = adapter
        }
    }

    override fun onItemClickListener(fond: Fond) {
        dismiss()
        findNavController().navigate(R.id.action_my_assets_fragment_to_add_fragment, bundleOf("fond" to fond, "type" to Converter.fromType(type)))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            val filteredFonds = Fonds()

            for(fond in fonds) {
                if (fond.ticker.lowercase().contains(newText.lowercase())
                    || fond.originalName.lowercase().contains(newText.lowercase())
                    || fond.name.lowercase().contains(newText.lowercase())) {
                    filteredFonds.add(fond)
                }
            }

            if (filteredFonds.isNotEmpty()) {
                adapter.fonds = filteredFonds
                adapter.notifyDataSetChanged()
            }
        }

        return false
    }
}