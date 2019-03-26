package com.suji.android.suji_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.adapter.FoodListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.callback.CreateMenuClick
import com.suji.android.suji_android.databinding.ActivityMainBinding
import com.suji.android.suji_android.food.CreateFoodDialog
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.DisplayHelper
import com.suji.android.suji_android.database.model.Food

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FoodListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initView()
        initOther()
    }

    private fun initViewModel() {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                if (foods != null) {
                    for (f in foods) {
                        Log.i("saveFood", f.name)
                    }
                    adapter.setFoodList(foods)
                }

                binding.executePendingBindings()
            }
        })
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = FoodListAdapter()
        layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        binding.callback = createMenu
        binding.mainFoodList.layoutManager = layoutManager
        binding.mainFoodList.adapter = adapter
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
