package com.suji.android.suji_android.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.database.model.Food
import kotlinx.android.synthetic.main.food_item.view.*

class FoodListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Food) {
        view.food_name.text = item.name
        view.food_price.text = item.price.toString()
    }
}