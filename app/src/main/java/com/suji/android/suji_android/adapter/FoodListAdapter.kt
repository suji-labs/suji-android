package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.food.FoodFragment
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.viewholders.FoodListViewHolder

class FoodListAdapter(
    private val fragmentVM: FoodViewModel?,
    private val clickHandler: FoodFragment.ClickHandler
) : RecyclerView.Adapter<FoodListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        return FoodListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.food_item, parent, false), clickHandler)
    }

    override fun getItemCount(): Int = fragmentVM?.foodList?.value?.size ?: 0

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        fragmentVM?.foodList?.value?.let {
            holder.bind(it[position])
        }
    }
}