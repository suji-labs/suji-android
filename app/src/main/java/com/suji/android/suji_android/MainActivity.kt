package com.suji.android.suji_android

import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suji.android.suji_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuViewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initView()
        initOther()
    }

    private fun initViewModel() {
        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        menuViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable memos: List<Food>?) {
//                if (memos != null) {
//                    adapter.setMemoList(memos)
//                }
//
//                binding.executePendingBindings()
            }
        })
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.callback = createMenu
    }

    private fun initOther() {
        DisplayHelper.Singleton.setContext(applicationContext)
    }

    private var createMenu: CreateMenuClick = object : CreateMenuClick {
        override fun onClick() {
//            val food = Food("보리밥", 6000)
//            menuViewModel.addFood(food)
            startActivity(Intent(applicationContext, CreateFoodDialog::class.java))
        }
    }
}
