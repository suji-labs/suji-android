package com.suji.android.suji_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.databinding.ViewPagerItemBinding


class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.Companion.ViewPagerViewHolder>() {
    private lateinit var items: ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
//        return MyViewHolder(view)

        val binding = DataBindingUtil
            .inflate<ViewPagerItemBinding>(
                LayoutInflater.from(parent.context), R.layout.view_pager_item,
                parent, false
            )
//        binding.callback = clickListener
        return ViewPagerAdapter.Companion.ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.binding.text = "Page $position"
    }

    override fun getItemCount(): Int {
//        return items.size
        return 3
    }

    fun setViewPagerItems(items: ArrayList<String>) {
        this.items = items
    }

    companion object {
        class ViewPagerViewHolder(val binding: ViewPagerItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}