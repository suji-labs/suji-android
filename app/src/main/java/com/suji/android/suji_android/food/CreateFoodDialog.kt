package com.suji.android.suji_android.food

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.suji.android.suji_android.R
import com.suji.android.suji_android.callback.CreateSubMenuClick
import com.suji.android.suji_android.databinding.CreateFoodBinding
import com.suji.android.suji_android.helper.DisplayHelper


class CreateFoodDialog : Activity() {
    private lateinit var binding: CreateFoodBinding
    private var subMenuID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        initView()
        val point = DisplayHelper.Singleton.getDisplaySize()
        window.attributes.width = (point.x * 0.9).toInt()
        window.attributes.height = (point.y * 0.7).toInt()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.create_food)
        binding.addSubMenu = addSubMenu
    }

    private var addSubMenu: CreateSubMenuClick = object :
        CreateSubMenuClick {
        override fun addSubMenuClick() {
            val layout: LinearLayout = findViewById(R.id.create_sub_menu)

            val label = BootstrapLabel(applicationContext)
            label.text = "부가 메뉴 이름"

            val editText = BootstrapEditText(applicationContext)
            editText.id = subMenuID

            layout.addView(label)
            layout.addView(editText)
        }
    }
}