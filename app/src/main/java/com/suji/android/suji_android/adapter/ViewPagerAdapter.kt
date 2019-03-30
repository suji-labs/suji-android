package com.suji.android.suji_android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.suji.android.suji_android.food.FoodFragment
import com.suji.android.suji_android.sell.SellFragment


class ViewPagerAdapter(private val tabCount: Int, fragmentManager: FragmentManager) : FragmentStateAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SellFragment()
            1 -> FoodFragment()
            else -> SellFragment()
        }
    }

    override fun getItemCount(): Int {
        return tabCount
    }
}