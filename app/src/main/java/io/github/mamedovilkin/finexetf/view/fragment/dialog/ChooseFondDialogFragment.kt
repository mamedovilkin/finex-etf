package io.github.mamedovilkin.finexetf.view.fragment.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentChooseFondDialogBinding
import io.github.mamedovilkin.finexetf.model.Fonds
import io.github.mamedovilkin.finexetf.view.adapter.FondRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.view.adapter.OnItemClickListener

class ChooseFondDialogFragment(private val fonds: Fonds) : BottomSheetDialogFragment(), OnItemClickListener {

    private var _binding: FragmentChooseFondDialogBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentChooseFondDialogBinding must not be null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseFondDialogBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            fondsRecyclerView.setHasFixedSize(true)
            fondsRecyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = FondRecyclerViewAdapter(fonds)
            adapter.onItemClickListener = this@ChooseFondDialogFragment
            fondsRecyclerView.adapter = adapter
        }
    }

    override fun onItemClickListener(position: Int) {
        dismiss()
        findNavController().navigate(R.id.action_my_assets_fragment_to_add_purchase_fragment, bundleOf("ticker" to fonds[position].ticker) )
    }
}