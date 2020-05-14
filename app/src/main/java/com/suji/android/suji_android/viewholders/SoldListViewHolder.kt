package com.suji.android.suji_android.viewholders

import android.os.Build
import android.view.View
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import kotlinx.android.synthetic.main.sold_item.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat

class SoldListViewHolder(private val view: View) : AndroidExtensionsViewHolder(view) {
    fun bind(item: Sale) {
        with (view) {
            if (item.isSale) {
                sold_item_name.text = "총 금액"
                sold_item_price.text =
                    DecimalFormat.getCurrencyInstance().format(item.price).toString()
                sold_item_description.text = ""

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    sold_item_date.text = item.time.toString()
                } else {
                    sold_item_date.text = LocalDateTime.ofInstant(Instant.ofEpochMilli(item.time), ZoneId.systemDefault()).format(Utils.format).toString()
                }
                setSoldDetail(item)
            }
        }
    }

    private fun setSoldDetail(item: Sale) {
        item.foods.iterator().let { iter ->
            while (iter.hasNext()) {
                iter.next().let {
                    if (item.pay == Constant.PayType.CARD) {
                        sold_pay.text = "카드"
                        sold_pay.bootstrapBrand =
                            DefaultBootstrapBrand.PRIMARY
                    } else {
                        sold_pay.text = "현금"
                        sold_pay.bootstrapBrand =
                            DefaultBootstrapBrand.SUCCESS
                    }
                    sold_item_description.text =
                        String.format(
                            view.context.getString(R.string.sell_item),
                            sold_item_description.text.toString(),
                            it.name,
                            it.count
                        )

                    sold_item_description.text =
                        sold_item_description.text.toString().trim()
                }
            }
        }
    }
}