package com.suji.android.suji_android.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.SellListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.SellFragmentBinding
import com.suji.android.suji_android.helper.DialogHelper
import com.suji.android.suji_android.listener.FloatingButtonClickListener

class SellFragment : Fragment() {
    private lateinit var binding: SellFragmentBinding
    private lateinit var adapter: SellListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var viewModel: SellViewModel = SellViewModel(BasicApp.app)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<SellFragmentBinding>(inflater, R.layout.sell_fragment, container, false)
        initViewModel()
        binding.listener = listener
        adapter = SellListAdapter()
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        binding.listener = createFood
//        binding.mainFoodList.layoutManager = layoutManager
//        binding.mainFoodList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private val listener: FloatingButtonClickListener = object : FloatingButtonClickListener {
        override fun sell() {
            DialogHelper(context!!, viewModel, R.layout.food_sell_dialog).show()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SellViewModel::class.java)
        viewModel.getAllSale().observe(this, object : Observer<List<Sale>> {
            override fun onChanged(@Nullable sales: List<Sale>?) {
                if (sales != null) {
                    adapter.setSaleList(sales)
                }

                binding.executePendingBindings()
            }
        })
    }
}