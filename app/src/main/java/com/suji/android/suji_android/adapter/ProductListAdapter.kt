package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.FoodItemBinding
import com.suji.android.suji_android.databinding.SellItemBinding
import com.suji.android.suji_android.databinding.SoldItemBinding
import com.suji.android.suji_android.helper.Constant

class ProductListAdapter(private val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<Any>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ViewDataBinding?
        when (this.viewType) {
            Constant.ViewType.FOOD_VIEW -> {
                binding = DataBindingUtil.inflate<FoodItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.food_item,
                    parent,
                    false
                )
                if (binding is FoodItemBinding) {
                    binding.delete = Constant.ListenerHashMap.listenerList["foodDeleteClickListener"]
                    binding.modify = Constant.ListenerHashMap.listenerList["foodModifyClickListener"]
                }
                return FoodViewHolder(binding)
            }
            Constant.ViewType.SALE_VIEW -> {
                binding = DataBindingUtil.inflate<SellItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.sell_item,
                    parent,
                    false
                )
                if (binding is SellItemBinding) {
                    binding.sell = Constant.ListenerHashMap.listenerList["foodSellClickListener"]
                    binding.modify = Constant.ListenerHashMap.listenerList["addSaleClickListener"]
                    binding.delete = Constant.ListenerHashMap.listenerList["foodSaleCancelClickListener"]
                }
                return SellViewHolder(binding)
            }
            else -> {
                binding = DataBindingUtil.inflate<SoldItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.sold_item,
                    parent,
                    false
                )
                return SoldViewHolder(binding)
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
                (items!![position] as Sale).foods.iterator().let { iter ->
                    while (iter.hasNext()) {
                        iter.next().let {
                            holder.binding.sellFoodDescription.text =
                                String.format(
                                    holder.binding.root.context.getString(R.string.sell_item),
                                    holder.binding.sellFoodDescription.text.toString(),
                                    it.name,
                                    it.count
                                )

                            if (it.sub.size != 0) {
                                for (item in it.sub) {
                                    holder.binding.sellFoodDescription.text =
                                        String.format(
                                            holder.binding.root.context.getString(R.string.sell_item),
                                            holder.binding.sellFoodDescription.text.toString(),
                                            item.name,
                                            item.count
                                        )
                                }
                            }

                            holder.binding.sellFoodDescription.text =
                                holder.binding.sellFoodDescription.text.toString().trim()
                        }
                    }
                }
                if ((items!![position] as Sale).foods.size == 0) {
                    holder.binding.sellFoodDescription.text = holder.binding.root.context.getString(R.string.no_sales)
                }
                holder.binding.executePendingBindings()
            }
            is SoldViewHolder -> {
                holder.binding.sale = items!![position] as Sale
                holder.binding.soldFoodDescription.text = ""
                (items!![position] as Sale).foods.iterator().let { iter ->
                    while (iter.hasNext()) {
                        iter.next().let {
                            if ((holder.binding.sale as Sale).pay == Constant.PayType.CARD) {
                                holder.binding.soldPay.text = "카드"
                                holder.binding.soldPay.bootstrapBrand = DefaultBootstrapBrand.PRIMARY
                            } else {
                                holder.binding.soldPay.text = "현금"
                                holder.binding.soldPay.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                            }
                            holder.binding.soldFoodDescription.text =
                                String.format(
                                    holder.binding.root.context.getString(R.string.sell_item),
                                    holder.binding.soldFoodDescription.text.toString(),
                                    it.name,
                                    it.count
                                )

                            holder.binding.soldFoodDescription.text =
                                holder.binding.soldFoodDescription.text.toString().trim()
                        }
                    }
                }
                holder.binding.executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            viewType -> Constant.ViewType.FOOD_VIEW
            viewType -> Constant.ViewType.SALE_VIEW
            else -> Constant.ViewType.SOLD_VIEW
        }
    }

    fun setItems(items: List<Any>?) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {
        class SoldViewHolder(val binding: SoldItemBinding) : RecyclerView.ViewHolder(binding.root)
        class FoodViewHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root)
        class SellViewHolder(val binding: SellItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}