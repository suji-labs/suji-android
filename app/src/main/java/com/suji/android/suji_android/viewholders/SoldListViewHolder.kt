package com.suji.android.suji_android.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.SoldItemBinding
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.text.DecimalFormat

class SoldListViewHolder(
    private val binding: SoldItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Sale) {
        if (item.isSale) {
            binding.soldItemName.text = "총 금액"
            binding.soldItemPrice.text =
                DecimalFormat.getCurrencyInstance().format(item.totalPrice).toString()
            binding.soldItemDescription.text = ""
            binding.soldItemDate.text = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(item.salesTime),
                ZoneId.systemDefault()
            ).format(Utils.format).toString()
            setSoldDetail(item)
        }
    }

    private fun setSoldDetail(item: Sale) {
        item.orderedFoods.iterator().let { iter ->
            while (iter.hasNext()) {
                iter.next().let {
                    if (item.pay == Constant.PayType.CARD) {
                        binding.soldPay.text = "카드"
                        binding.soldPay.bootstrapBrand =
                            DefaultBootstrapBrand.PRIMARY
                    } else {
                        binding.soldPay.text = "현금"
                        binding.soldPay.bootstrapBrand =
                            DefaultBootstrapBrand.SUCCESS
                    }
                    binding.soldItemDescription.text =
                        String.format(
                            binding.root.context.getString(R.string.sell_item),
                            binding.soldItemDescription.text.toString(),
                            it.name,
                            it.count
                        )

                    binding.soldItemDescription.text =
                        binding.soldItemDescription.text.toString().trim()
                }
            }
        }
    }
}