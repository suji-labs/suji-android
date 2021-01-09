package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.account.AccountViewModel
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.viewholders.SoldListViewHolder

class SoldListAdapter(
    private val fragmentVM: AccountViewModel?
) : RecyclerView.Adapter<SoldListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldListViewHolder {
        return SoldListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.sold_item, parent, false))
    }

    override fun getItemCount(): Int = fragmentVM?.soldList?.value?.size ?: 0

    override fun onBindViewHolder(holder: SoldListViewHolder, position: Int) {
        fragmentVM?.soldList?.value?.get(position)?.let {
            holder.bind(it)
        }
    }
}