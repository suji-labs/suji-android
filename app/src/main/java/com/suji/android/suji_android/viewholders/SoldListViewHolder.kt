package com.suji.android.suji_android.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import kotlinx.android.synthetic.main.sold_item.view.*
import org.joda.time.DateTime
import java.text.DecimalFormat

class SoldListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Sale) {
        if (item.isSale) {
            view.sold_item_name.text = "총 금액"
            view.sold_item_price.text = DecimalFormat.getCurrencyInstance().format(item.price).toString()
            view.sold_item_description.text = ""
            view.sold_item_date.text = DateTime(item.time).toString(Utils.format)
            item.foods.iterator().let { iter ->
                while (iter.hasNext()) {
                    iter.next().let {
                        if (item.pay == Constant.PayType.CARD) {
                            view.sold_pay.text = "카드"
                            view.sold_pay.bootstrapBrand =
                                DefaultBootstrapBrand.PRIMARY
                        } else {
                            view.sold_pay.text = "현금"
                            view.sold_pay.bootstrapBrand =
                                DefaultBootstrapBrand.SUCCESS
                        }
                        view.sold_item_description.text =
                            String.format(
                                view.context.getString(R.string.sell_item),
                                view.sold_item_description.text.toString(),
                                it.name,
                                it.count
                            )

                        view.sold_item_description.text = view.sold_item_description.text.toString().trim()
                    }
                }
            }
        }
    }
}