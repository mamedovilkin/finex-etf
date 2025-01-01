package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.databinding.FragmentMyAssetsBinding
import io.github.mamedovilkin.finexetf.model.Fonds
import io.github.mamedovilkin.finexetf.room.Fond
import io.github.mamedovilkin.finexetf.room.Type
import io.github.mamedovilkin.finexetf.view.adapter.CardViewPagerFragmentStateAdapter
import io.github.mamedovilkin.finexetf.view.fragment.card.DollarCardFragment
import io.github.mamedovilkin.finexetf.view.fragment.card.RubleCardFragment
import io.github.mamedovilkin.finexetf.view.fragment.dialog.ChooseFondDialogFragment
import io.github.mamedovilkin.finexetf.viewmodel.MyAssetsViewModel

@AndroidEntryPoint
class MyAssetsFragment : Fragment() {

    private var _binding: FragmentMyAssetsBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentMyAssetsBinding must not be null")
    private lateinit var remoteFonds: Fonds
    private lateinit var localFonds: List<Fond>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyAssetsBinding.inflate(inflater)

        val viewModel = ViewModelProvider(requireActivity())[MyAssetsViewModel::class]

        viewModel.remoteFonds.observe(viewLifecycleOwner) {
            remoteFonds = it
            binding.addPurchase.visibility = View.VISIBLE
        }

        viewModel.localFonds.observe(viewLifecycleOwner) {
            localFonds = it
            if (localFonds.isNotEmpty()) {
                binding.assetsRecyclerView.visibility = View.VISIBLE
                binding.placeholderLinearLayout.visibility = View.GONE
                binding.addSell.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewPager2.adapter = CardViewPagerFragmentStateAdapter(listOf(RubleCardFragment(), DollarCardFragment()), childFragmentManager, viewLifecycleOwner.lifecycle)
            circleIndicator3.setViewPager(viewPager2)

            addPurchase.setOnClickListener {
                if (!remoteFonds.isEmpty()) {
                    ChooseFondDialogFragment(remoteFonds, Type.PURCHASE).show(parentFragmentManager, "FondListDialogFragment")
                }
            }

            addSell.setOnClickListener {
                if (localFonds.isNotEmpty()) {
                    ChooseFondDialogFragment(arrayListOf(io.github.mamedovilkin.finexetf.model.Fond(localFonds[0].ticker, localFonds[0].icon, localFonds[0].name, localFonds[0].originalName)), Type.SELL).show(parentFragmentManager, "FondListDialogFragment")
                }
            }
        }
    }
}