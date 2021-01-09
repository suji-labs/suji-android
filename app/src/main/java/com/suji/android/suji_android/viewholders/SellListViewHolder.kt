package com.suji.android.suji_android.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.SellItemBinding
import com.suji.android.suji_android.sell.SellFragment
import java.text.DecimalFormat

class SellListViewHolder(
    private val binding: SellItemBinding,
    private val clickHandler: SellFragment.ClickHandler
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Sale) {
        binding.sellItemSold.setOnClickListener {
            clickHandler.sellItem(item)
        }
        binding.sellItemModify.setOnClickListener {
            clickHandler.modifySaleItem(item)
        }
        binding.sellItemDelete.setOnClickListener {
            clickHandler.deleteSaleItem(item)
        }

        binding.sellItemName.text = item.name
        binding.sellItemPrice.text =
            DecimalFormat.getCurrencyInstance().format(item.totalPrice).toString()
        binding.sellItemDescription.text = ""

        if (item.orderedFoods.size == 0) {
            binding.sellItemDescription.text = binding.root.context.getString(R.string.no_sales)
        } else {
            setSellDetail(item)
        }
    }

    private fun setSellDetail(item: Sale) {
        item.orderedFoods.iterator().let { iter ->
            while (iter.hasNext()) {
                iter.next().let {
                    binding.sellItemDescription.text = String.format(
                        binding.root.context.getString(R.string.sell_item) + ", ",
                        binding.sellItemDescription.text.toString(),
                        it.name,
                        it.count
                    )
//                    if (iter.hasNext()) {
//                        sell_item_description.text = String.format(
//                            view.context.getString(R.string.sell_item) + ", ",
//                            sell_item_description.text.toString(),
//                            it.name,
//                            it.count
//                        )
//                    } else {
//                        sell_item_description.text = String.format(
//                            view.context.getString(R.string.sell_item),
//                            sell_item_description.text.toString(),
//                            it.name,
//                            it.count
//                        )
//                    }

                    if (it.subMenu.size != 0) {
                        for (item in it.subMenu) {
                            if (item.count != 0) {
                                binding.sellItemDescription.text = String.format(
                                    binding.root.context.getString(R.string.sell_item) + ", ",
                                    binding.sellItemDescription.text.toString(),
                                    item.name,
                                    item.count
                                )
                            }
                        }
                    }

                    binding.sellItemDescription.text = binding.sellItemDescription.text.toString().trim()
                }
            }
        }
    }
}