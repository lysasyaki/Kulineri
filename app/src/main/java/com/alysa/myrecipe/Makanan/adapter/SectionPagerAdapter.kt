package com.alysa.myrecipe.Makanan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alysa.myrecipe.Makanan.presentation.AsiaFragment
import com.alysa.myrecipe.Makanan.presentation.BaratFragment
import com.alysa.myrecipe.Makanan.presentation.ModernFragment
import com.alysa.myrecipe.Makanan.presentation.TradisionalFragment

class SectionPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TradisionalFragment()
            1 -> ModernFragment()
            2 -> BaratFragment()
            3 -> AsiaFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}