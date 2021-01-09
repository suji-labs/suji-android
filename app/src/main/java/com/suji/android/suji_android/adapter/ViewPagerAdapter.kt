package com.suji.android.suji_android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.suji.android.suji_android.account.AccountFragment
import com.suji.android.suji_android.activities.MainActivity
import com.suji.android.suji_android.food.FoodFragment
import com.suji.android.suji_android.sell.SellFragment


class ViewPagerAdapter(
    private val mainTab: MutableList<MainActivity.Tab>?,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SellFragment()
            1 -> AccountFragment()
            2 -> FoodFragment()
            else -> throw IllegalAccessException("not found fragment")
        }
    }

    override fun getItemCount(): Int {
        return mainTab?.size ?: 0
    }
}