package io.github.mamedovilkin.finexetf.view.adapter.myassets

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class NetWorthFragmentStateAdapter(
    private val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}