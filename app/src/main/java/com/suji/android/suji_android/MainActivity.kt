package com.suji.android.suji_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.suji.android.suji_android.callback.CreateMenuClick
import com.suji.android.suji_android.databinding.ActivityMainBinding
import com.suji.android.suji_android.food.CreateFoodDialog
import com.suji.android.suji_android.helper.DisplayHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initOther()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.callback = createMenu
    }

    private fun initOther() {
        DisplayHelper.Singleton.setContext(applicationContext)
    }

    private var createMenu: CreateMenuClick = object :
        CreateMenuClick {
        override fun onClick() {
            startActivity(Intent(applicationContext, CreateFoodDialog::class.java))
        }
    }
}
