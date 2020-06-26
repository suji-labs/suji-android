package com.suji.android.suji_android.viewholders

import android.view.View
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.listener.ItemClickListener
import kotlinx.android.synthetic.main.food_item.*

class FoodListViewHolder(private val view: View, private val listener: ItemClickListener) : AndroidExtensionsViewHolder(view) {
    fun bind(item: Food) {
        with (view) {
            food_name.text = item.name
            food_price.text = item.price.toString()
            food_sub_menu.text = item.subMenu.size.toString()
            food_modify.setOnClickListener {
                listener.onItemClick(it, item)
            }
            food_delete.setOnClickListener {
                listener.onItemClick(it, item)
            }
        }
    }
}