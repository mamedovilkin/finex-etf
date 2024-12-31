package io.github.mamedovilkin.finexetf.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.mamedovilkin.finexetf.view.fragment.card.DollarCardFragment
import io.github.mamedovilkin.finexetf.view.fragment.card.RubleCardFragment

class CardFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RubleCardFragment()
            1 -> DollarCardFragment()
            else -> RubleCardFragment()
        }
    }
}