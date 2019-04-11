package com.suji.android.suji_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food

class FoodSaleListAdapter : BaseAdapter() {
    private var items: List<Food>? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            (parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).let {
                view = it.inflate(R.layout.spinner_item, parent, false)
            }
        }

        view!!.findViewById<TextView>(R.id.spinner_item_name).text = items!![position].name

        return view as View
    }

    override fun getItem(position: Int): Any {
        return items!![position]
    }

    override fun getItemId(position: Int): Long {
        return items!![position].id.toLong()
    }

    override fun getCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            (parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).let {
                view = it.inflate(R.layout.spinner_item, parent, false)
            }
        }

        view!!.findViewById<TextView>(R.id.spinner_item_name).text = items!![position].name

        return view as View
    }

    fun setItems(items: List<Food>) {
        this.items = items
        notifyDataSetChanged()
    }
}