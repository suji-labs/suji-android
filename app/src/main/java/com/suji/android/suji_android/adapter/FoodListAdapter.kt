package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.viewholders.FoodListViewHolder

class FoodListAdapter : RecyclerView.Adapter<FoodListViewHolder>() {
    private var items: List<Food>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        holder.bind(items!![position])
    }

    fun setItems(items: List<Food>?) {
        this.items = items
        notifyDataSetChanged()
    }
}