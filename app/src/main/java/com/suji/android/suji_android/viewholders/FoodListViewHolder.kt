package com.suji.android.suji_android.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.listener.ItemClickListener
import kotlinx.android.synthetic.main.food_item.view.*

class FoodListViewHolder(private val view: View, private val listener: ItemClickListener) : RecyclerView.ViewHolder(view) {
    fun bind(item: Food) {
        view.food_name.text = item.name
        view.food_price.text = item.price.toString()
        view.food_sub_menu.text = item.sub.size.toString()
        view.food_modify.setOnClickListener {
            listener.onItemClick(it, item)
        }
        view.food_delete.setOnClickListener {
            listener.onItemClick(it, item)
        }
    }
}