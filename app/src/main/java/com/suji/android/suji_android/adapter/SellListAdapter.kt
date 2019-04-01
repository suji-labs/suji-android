package com.suji.android.suji_android.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.FoodItemBinding
import java.util.*

class SellListAdapter :
    RecyclerView.Adapter<FoodListAdapter.Companion.FoodViewHolder>() {
    private var items: List<Sale>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListAdapter.Companion.FoodViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: FoodListAdapter.Companion.FoodViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setSaleList(saleList: List<Sale>?) {
        if (this.items == null) {
            this.items = saleList
            notifyItemRangeInserted(0, saleList!!.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun getOldListSize(): Int {
                    return items!!.size
                }

                override fun getNewListSize(): Int {
                    return saleList!!.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return items!![oldItemPosition].id == saleList!![newItemPosition].id
                }

                override fun areContentsTheSame(newItemPosition: Int, oldItemPosition: Int): Boolean {
                    val newProduct = saleList!![oldItemPosition]
                    val oldProduct = items!![newItemPosition]
                    return (newProduct.id == oldProduct.id
                            && Objects.equals(newProduct.name, oldProduct.name)
                            && Objects.equals(newProduct.price, oldProduct.price))
                }
            })
            items = saleList
            result.dispatchUpdatesTo(this)
        }
    }

    companion object {
        class FoodViewHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}