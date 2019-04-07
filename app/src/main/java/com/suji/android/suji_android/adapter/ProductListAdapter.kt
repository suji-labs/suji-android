package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.FoodItemBinding
import com.suji.android.suji_android.databinding.SellItemBinding
import com.suji.android.suji_android.databinding.SoldItemBinding
import com.suji.android.suji_android.helper.ViewType.FOOD_VIEW
import com.suji.android.suji_android.helper.ViewType.SALE_VIEW
import com.suji.android.suji_android.helper.ViewType.SOLD_VIEW
import com.suji.android.suji_android.listener.FoodClickListener

class ProductListAdapter(private val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<Any>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var binding: ViewDataBinding? = null
        when (this.viewType) {
            FOOD_VIEW -> {
                binding = DataBindingUtil.inflate<FoodItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.food_item,
                    parent,
                    false
                )
                return FoodViewHolder(binding)
            }
            SALE_VIEW -> {
                binding = DataBindingUtil.inflate<SellItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.sell_item,
                    parent,
                    false
                )
                return SellViewHolder(binding)
            }
            else -> {
                binding = DataBindingUtil.inflate<SoldItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.sold_item,
                    parent,
                    false
                )
                return ProductListViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FoodViewHolder -> {
                holder.binding.food = items!![position] as Food
                holder.binding.executePendingBindings()
            }
            is SellViewHolder -> {
                holder.binding.sale = items!![position] as Sale
                holder.binding.sellFoodDescription.text = ""
                val iter = (items!![position] as Sale).foods.iterator()
                while (iter.hasNext()) {
                    val f = iter.next()
                    holder.binding.sellFoodDescription.text =
                        String.format(
                            holder.binding.root.context.getString(R.string.sell_item),
                            holder.binding.sellFoodDescription.text.toString(),
                            f.name,
                            f.count
                        )

                    holder.binding.sellFoodDescription.text =
                        holder.binding.sellFoodDescription.text.toString().trim()
                }
                holder.binding.executePendingBindings()
            }
            is ProductListViewHolder -> {
                holder.binding.soldItemName.text = (items!![position] as Sale).name
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            viewType -> FOOD_VIEW
            viewType -> SALE_VIEW
            else -> SOLD_VIEW
        }
    }

    fun setItems(saleList: List<Any>?) {
        if (this.items == null) {
            this.items = saleList
            notifyItemRangeInserted(0, saleList!!.size)
        } else {
            items = saleList
        }
    }

    companion object {
        class ProductListViewHolder(var binding: SoldItemBinding) : RecyclerView.ViewHolder(binding.root)
        class FoodViewHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root)
        class SellViewHolder(val binding: SellItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}