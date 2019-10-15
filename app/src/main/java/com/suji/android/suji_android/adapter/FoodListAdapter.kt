package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.listener.ItemClickListener
import com.suji.android.suji_android.viewholders.FoodListViewHolder

class FoodListAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<FoodListViewHolder>() {
    private var items = ArrayList() ?: emptyList<Food>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodListViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<Food>) {
        this.items = items
        notifyDataSetChanged()
    }
}