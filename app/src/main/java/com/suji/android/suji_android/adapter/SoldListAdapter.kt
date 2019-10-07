package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.viewholders.SoldListViewHolder

class SoldListAdapter : RecyclerView.Adapter<SoldListViewHolder>() {
    private var items: List<Sale>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sold_item, parent, false)
        return SoldListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun onBindViewHolder(holder: SoldListViewHolder, position: Int) {
        holder.bind(items!![position])
    }

    fun setItems(items: List<Sale>?) {
        this.items = items
        notifyDataSetChanged()
    }
}