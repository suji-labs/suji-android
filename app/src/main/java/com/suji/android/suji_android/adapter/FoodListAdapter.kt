package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.callback.DeleteFoodClick
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodItemBinding
import java.util.*

class FoodListAdapter(var clickListener: DeleteFoodClick) :
    RecyclerView.Adapter<FoodListAdapter.Companion.FoodViewHolder>() {
    private var items: List<Food>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = DataBindingUtil
            .inflate<FoodItemBinding>(
                LayoutInflater.from(parent.context), R.layout.food_item,
                parent, false
            )
        binding.callback = clickListener
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.binding.food = items!![position]
        holder.binding.executePendingBindings()
    }

    fun setFoodList(foodList: List<Food>?) {
        if (this.items == null) {
            this.items = foodList
            notifyItemRangeInserted(0, foodList!!.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun getOldListSize(): Int {
                    return items!!.size
                }

                override fun getNewListSize(): Int {
                    return foodList!!.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return items!![oldItemPosition].id == foodList!![newItemPosition].id
                }

                override fun areContentsTheSame(newItemPosition: Int, oldItemPosition: Int): Boolean {
                    val newProduct = foodList!![oldItemPosition]
                    val oldProduct = items!![newItemPosition]
                    return (newProduct.id == oldProduct.id
                            && Objects.equals(newProduct.name, oldProduct.name)
                            && Objects.equals(newProduct.price, oldProduct.price))
                }
            })
            items = foodList
            result.dispatchUpdatesTo(this)
        }
    }

    companion object {
        class FoodViewHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}