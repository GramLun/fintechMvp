package com.moskofidi.fintech.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arellomobile.mvp.MvpActivity
import com.arellomobile.mvp.MvpFragment
import com.moskofidi.fintech.mvp.fragments.HotFragment
import com.moskofidi.fintech.mvp.fragments.LatestFragment
import com.moskofidi.fintech.mvp.fragments.TopFragment

class TabAdapter(activity: MvpActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                LatestFragment()
            }
            1 -> TopFragment()
            else -> {
                return HotFragment()
            }
        }
    }
}