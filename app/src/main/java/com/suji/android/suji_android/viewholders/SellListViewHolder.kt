package com.suji.android.suji_android.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.listener.ItemClickListener
import kotlinx.android.synthetic.main.sell_item.view.*
import java.text.DecimalFormat

class SellListViewHolder(private val view: View, private val listener: ItemClickListener) : RecyclerView.ViewHolder(view) {
    fun bind(item: Sale) {
        view.sell_item_sold.setOnClickListener {
            listener.onItemClick(it, item)
        }
        view.sell_item_modify.setOnClickListener {
            listener.onItemClick(it, item)
        }
        view.sell_item_delete.setOnClickListener {
            listener.onItemClick(it, item)
        }

        view.sell_item_name.text = "총 금액"
        view.sell_item_price.text =
            DecimalFormat.getCurrencyInstance().format(item.price).toString()
        view.sell_item_description.text = ""
        item.foods.iterator().let { iter ->
            while (iter.hasNext()) {
                iter.next().let {
                    view.sell_item_description.text =
                        String.format(
                            view.context.getString(R.string.sell_item),
                            view.sell_item_description.text.toString(),
                            it.name,
                            it.count
                        )

                    if (it.sub.size != 0) {
                        for (item in it.sub) {
                            if (item.count != 0) {
                                view.sell_item_description.text =
                                    String.format(
                                        view.context.getString(R.string.sell_item),
                                        view.sell_item_description.text.toString(),
                                        item.name,
                                        item.count
                                    )
                            }
                        }
                    }

                    view.sell_item_description.text =
                        view.sell_item_description.text.toString().trim()
                }
            }
        }
        if (item.foods.size == 0) {
            view.sell_item_description.text = view.context.getString(R.string.no_sales)
        }
    }
}