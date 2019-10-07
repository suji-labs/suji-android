package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils.format
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.sell_item.view.*
import kotlinx.android.synthetic.main.sold_item.view.*
import org.joda.time.DateTime
import java.text.DecimalFormat

class ProductListAdapter(private val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<Any>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (this.viewType) {
            Constant.ViewType.FOOD_VIEW -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
                FoodViewHolder(view)
            }
            Constant.ViewType.SALE_VIEW -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.sell_item, parent, false)
                SellViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.sold_item, parent, false)
                SoldViewHolder(view)
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
                val food = items!![position] as Food
                holder.view.food_name.text = food.name
                holder.view.food_price.text = food.price.toString()
            }
            is SellViewHolder -> {
                val sale = items!![position] as Sale
                holder.view.sell_item_name.text = "총 금액"
                holder.view.sell_item_price.text = DecimalFormat.getCurrencyInstance().format(sale.price).toString()
                holder.view.sell_item_description.text = ""
                sale.foods.iterator().let { iter ->
                    while (iter.hasNext()) {
                        iter.next().let {
                            holder.view.sell_item_description.text =
                                String.format(
                                    holder.view.context.getString(R.string.sell_item),
                                    holder.view.sell_item_description.text.toString(),
                                    it.name,
                                    it.count
                                )

                            if (it.sub.size != 0) {
                                for (item in it.sub) {
                                    if (item.count != 0) {
                                        holder.view.sell_item_description.text =
                                            String.format(
                                                holder.view.context.getString(R.string.sell_item),
                                                holder.view.sell_item_description.text.toString(),
                                                item.name,
                                                item.count
                                            )
                                    }
                                }
                            }

                            holder.view.sell_item_description.text =
                                holder.view.sell_item_description.text.toString().trim()
                        }
                    }
                }
                if ((items!![position] as Sale).foods.size == 0) {
                    holder.view.sell_item_description.text =
                        holder.view.context.getString(R.string.no_sales)
                }
            }
            is SoldViewHolder -> {
                val sold = items!![position] as Sale
                holder.view.sold_item_name.text = "총 금액"
                holder.view.sold_item_price.text = DecimalFormat.getCurrencyInstance().format(sold.price).toString()
                holder.view.sold_item_description.text = ""
                holder.view.sold_item_date.text =
                    DateTime(sold.time).toString(format)
                (items!![position] as Sale).foods.iterator().let { iter ->
                    while (iter.hasNext()) {
                        iter.next().let {
                            if (sold.pay == Constant.PayType.CARD) {
                                holder.view.sold_pay.text = "카드"
                                holder.view.sold_pay.bootstrapBrand =
                                    DefaultBootstrapBrand.PRIMARY
                            } else {
                                holder.view.sold_pay.text = "현금"
                                holder.view.sold_pay.bootstrapBrand =
                                    DefaultBootstrapBrand.SUCCESS
                            }
                            holder.view.sold_item_description.text =
                                String.format(
                                    holder.view.context.getString(R.string.sell_item),
                                    holder.view.sold_item_description.text.toString(),
                                    it.name,
                                    it.count
                                )

                            holder.view.sold_item_description.text =
                                holder.view.sold_item_description.text.toString().trim()
                        }
                    }
                }
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
        class SoldViewHolder(val view: View) : RecyclerView.ViewHolder(view)
        class FoodViewHolder(val view: View) : RecyclerView.ViewHolder(view)
        class SellViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    }
}