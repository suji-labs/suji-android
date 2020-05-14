package com.suji.android.suji_android.viewholders

import android.view.View
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.listener.ItemClickListener
import kotlinx.android.synthetic.main.sell_item.*
import java.text.DecimalFormat

class SellListViewHolder(private val view: View, private val listener: ItemClickListener) : AndroidExtensionsViewHolder(view) {
    fun bind(item: Sale) {
        with(view) {
            sell_item_sold.setOnClickListener {
                listener.onItemClick(it, item)
            }
            sell_item_modify.setOnClickListener {
                listener.onItemClick(it, item)
            }
            sell_item_delete.setOnClickListener {
                listener.onItemClick(it, item)
            }

            sell_item_name.text = item.name
            sell_item_price.text =
                DecimalFormat.getCurrencyInstance().format(item.price).toString()
            sell_item_description.text = ""

            if (item.foods.size == 0) {
                sell_item_description.text = view.context.getString(R.string.no_sales)
            } else {
                setSellDetail(item)
            }
        }
    }

    private fun setSellDetail(item: Sale) {
        item.foods.iterator().let { iter ->
            while (iter.hasNext()) {
                iter.next().let {
                    sell_item_description.text =
                        String.format(
                            view.context.getString(R.string.sell_item),
                            sell_item_description.text.toString(),
                            it.name,
                            it.count
                        )

                    if (it.sub.size != 0) {
                        for (item in it.sub) {
                            if (item.count != 0) {
                                sell_item_description.text =
                                    String.format(
                                        view.context.getString(R.string.sell_item),
                                        sell_item_description.text.toString(),
                                        item.name,
                                        item.count
                                    )
                            }
                        }
                    }

                    sell_item_description.text =
                        sell_item_description.text.toString().trim()
                }
            }
        }
    }
}