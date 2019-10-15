package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.listener.ItemClickListener
import com.suji.android.suji_android.viewholders.SellListViewHolder

class SellListAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<SellListViewHolder>() {
    private var items = ArrayList<Sale>() ?: emptyList<Sale>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sell_item, parent, false)
        return SellListViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SellListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<Sale>) {
        this.items = items
        notifyDataSetChanged()
    }
}