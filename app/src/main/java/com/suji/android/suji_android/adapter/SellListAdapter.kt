package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.FoodItemBinding
import com.suji.android.suji_android.databinding.SellItemBinding
import com.suji.android.suji_android.listener.FoodSellClickListener
import java.util.*

class SellListAdapter(var listener: FoodSellClickListener) :
    RecyclerView.Adapter<SellListAdapter.Companion.SellViewHolder>() {
    private var items: List<Sale>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellListAdapter.Companion.SellViewHolder {
        val binding = DataBindingUtil
            .inflate<SellItemBinding>(
                LayoutInflater.from(parent.context), R.layout.sell_item,
                parent, false
            )
        binding.listener = listener
        return SellViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun onBindViewHolder(holder: SellListAdapter.Companion.SellViewHolder, position: Int) {
        holder.binding.sale = items!![position]
        holder.binding.executePendingBindings()
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
        class SellViewHolder(val binding: SellItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}