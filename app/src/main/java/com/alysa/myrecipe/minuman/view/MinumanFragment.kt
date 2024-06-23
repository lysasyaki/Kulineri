package com.alysa.myrecipe.minuman.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alysa.myrecipe.Makanan.adapter.SectionPagerAdapter
import com.alysa.myrecipe.R
import com.alysa.myrecipe.minuman.adapter.SectionMinumanAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MinumanFragment : Fragment(){
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drink, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager(view)
    }

    private fun setViewPager(view: View) {
        val viewPagerAdapter =
            SectionMinumanAdapter(childFragmentManager, lifecycle)
        viewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = true
        tabLayout = view.findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()

    }

    companion object {
        private val TAB_TITLE = intArrayOf(
            R.string.tradisional,
            R.string.modern,
        )
    }
}
