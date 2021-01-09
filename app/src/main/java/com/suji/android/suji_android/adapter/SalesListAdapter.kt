package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.food.FoodViewModel

class SalesListAdapter(
    private val fragmentVM: FoodViewModel?
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.spinner_item, parent, false)

        view.findViewById<TextView>(R.id.spinner_item_name).text = fragmentVM?.foodList?.value?.get(position)?.name

        return view
    }

    override fun getItem(position: Int): Any = fragmentVM?.foodList?.value?.get(position) ?: Food()

    override fun getItemId(position: Int): Long = fragmentVM?.foodList?.value?.get(position)?.id?.toLong() ?: 0L

    override fun getCount(): Int = fragmentVM?.foodList?.value?.size ?: 0

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.spinner_item, parent, false)

        view.findViewById<TextView>(R.id.spinner_item_name).text = fragmentVM?.foodList?.value?.get(position)?.name

        return view
    }
}