package com.suji.android.suji_android.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.listener.CreateFoodClickListener
import com.suji.android.suji_android.listener.FoodClickListener
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodFragmentBinding
import com.suji.android.suji_android.helper.DialogHelper

class FoodFragment : Fragment() {
    private lateinit var binding: FoodFragmentBinding
    private lateinit var adapter: FoodListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FoodFragmentBinding>(inflater, R.layout.food_fragment, container, false)
        initViewModel()
        adapter = FoodListAdapter(foodClickListener)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.listener = createFood
        binding.mainFoodList.layoutManager = layoutManager
        binding.mainFoodList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                if (foods != null) {
                    adapter.setFoodList(foods)
                }

                binding.executePendingBindings()
            }
        })
    }

    private var createFood: CreateFoodClickListener = object : CreateFoodClickListener {
        override fun onClick() {
            DialogHelper(context!!, R.layout.food_create_dialog, foodViewModel).show()
        }
    }

    private var foodClickListener: FoodClickListener = object : FoodClickListener {
        override fun onDeleteClick(food: Food) {
            foodViewModel.delete(food)
        }

        override fun onModifyClick(food: Food) {
            DialogHelper(context!!, R.layout.food_create_dialog, food, foodViewModel).show()
        }
    }
}