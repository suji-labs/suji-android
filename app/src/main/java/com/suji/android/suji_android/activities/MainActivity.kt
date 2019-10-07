package com.suji.android.suji_android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        main_tab_layout.addTab(main_tab_layout.newTab().setText("장사"))
        main_tab_layout.addTab(main_tab_layout.newTab().setText("장부"))
        main_tab_layout.addTab(main_tab_layout.newTab().setText("메뉴"))

        main_view_pager.adapter = ViewPagerAdapter(main_tab_layout.tabCount, supportFragmentManager, lifecycle)
        main_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                main_tab_layout.selectTab(main_tab_layout.getTabAt(position))
            }
        })
        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_view_pager.currentItem = tab!!.position
            }
        })
    }
}
