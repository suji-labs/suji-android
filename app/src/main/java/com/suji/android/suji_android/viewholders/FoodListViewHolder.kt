package com.suji.android.suji_android.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodItemBinding
import com.suji.android.suji_android.food.FoodFragment

class FoodListViewHolder(
    private val binding: FoodItemBinding,
    private val clickHandler: FoodFragment.ClickHandler
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Food) {
        binding.foodName.text = item.name
        binding.foodPrice.text = item.price.toString()
        binding.foodSubMenu.text = item.subMenu.size.toString()
        binding.foodModify.setOnClickListener {
            clickHandler.modifyFood(item)
        }
        binding.foodDelete.setOnClickListener {
            clickHandler.deleteFood(item)
        }
    }
}