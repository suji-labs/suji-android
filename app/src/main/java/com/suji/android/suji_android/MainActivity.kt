package com.suji.android.suji_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.suji.android.suji_android.adapter.ViewPagerAdapter
import com.suji.android.suji_android.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainTabLayout.addTab(binding.mainTabLayout.newTab().setText("장사"))
        binding.mainTabLayout.addTab(binding.mainTabLayout.newTab().setText("장부"))
        binding.mainTabLayout.addTab(binding.mainTabLayout.newTab().setText("메뉴"))

        binding.mainViewPager.adapter = ViewPagerAdapter(binding.mainTabLayout.tabCount, supportFragmentManager)
        binding.mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.mainTabLayout.selectTab(binding.mainTabLayout.getTabAt(position))
            }
        })
        binding.mainTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.mainViewPager.currentItem = tab!!.position
            }
        })
    }
}
