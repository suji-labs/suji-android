package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.sell.SellFragment
import com.suji.android.suji_android.sell.SellViewModel
import com.suji.android.suji_android.viewholders.SellListViewHolder

class SellListAdapter(
    private val fragmentVM: SellViewModel?,
    private val clickHandler: SellFragment.ClickHandler
) : RecyclerView.Adapter<SellListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellListViewHolder {
        return SellListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.sell_item, parent, false), clickHandler)
    }

    override fun getItemCount(): Int = fragmentVM?.sellList?.value?.size ?: 0

    override fun onBindViewHolder(holder: SellListViewHolder, position: Int) {
        fragmentVM?.sellList?.value?.let {
            holder.bind(it[position])
        }
    }
}