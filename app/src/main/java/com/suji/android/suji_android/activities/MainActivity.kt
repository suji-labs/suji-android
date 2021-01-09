package com.suji.android.suji_android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.ViewPagerAdapter
import com.suji.android.suji_android.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mainTab = mutableListOf<Tab>(
        Tab("장사"),
        Tab("장부"),
        Tab("메뉴")
    )
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    data class Tab(val title: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding.mainViewPager.adapter = ViewPagerAdapter(mainTab, supportFragmentManager, lifecycle)
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, index ->
            if (index < mainTab.size) {
                tab.text = mainTab[index].title
            }
        }.attach()
    }
}
