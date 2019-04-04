package com.suji.android.suji_android.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.SellFragmentBinding
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.DialogHelper
import com.suji.android.suji_android.listener.FloatingButtonClickListener
import com.suji.android.suji_android.listener.FoodSellClickListener

class SellFragment : Fragment() {
    private lateinit var binding: SellFragmentBinding
    private lateinit var adapter: SellListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var sellViewModel: SellViewModel = SellViewModel(BasicApp.app)
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)
    private var foods: List<Food>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<SellFragmentBinding>(inflater, R.layout.sell_fragment, container, false)
        initViewModel()
        binding.listener = floatingButtonClickListener
        adapter = SellListAdapter(foodSellClickListener)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.sellFragmentItems.layoutManager = layoutManager
        binding.sellFragmentItems.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private val floatingButtonClickListener: FloatingButtonClickListener = object : FloatingButtonClickListener {
        override fun sell() {
//            DialogHelper(context!!, R.layout.food_sell_dialog, sellViewModel, foods as ArrayList<Food>).show()
        }
    }

    private val foodSellClickListener: FoodSellClickListener = object : FoodSellClickListener {
        override fun sell(sale: Sale) {
            sellViewModel.delete(sale)
        }

        override fun addFood() {
            Toast.makeText(context, "addFood", Toast.LENGTH_SHORT).show()
        }

        override fun cancel(sale: Sale) {
            sellViewModel.delete(sale)
        }
    }

    private fun initViewModel() {
        sellViewModel = ViewModelProviders.of(this).get(SellViewModel::class.java)
        sellViewModel.getAllSale().observe(this, object : Observer<List<Sale>> {
            override fun onChanged(@Nullable sales: List<Sale>?) {
                if (sales != null) {
                    adapter.setSaleList(sales)
                }

                binding.executePendingBindings()
            }
        })

        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                if (foods != null) {
                    this@SellFragment.foods = foods
                }
            }
        })
    }
}