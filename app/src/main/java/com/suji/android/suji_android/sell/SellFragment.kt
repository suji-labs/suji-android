package com.suji.android.suji_android.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.suji.android.suji_android.R
import com.suji.android.suji_android.databinding.SellFragmentBinding
import com.suji.android.suji_android.listener.SellFoodClickListener

class SellFragment : Fragment() {
    private lateinit var binding: SellFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<SellFragmentBinding>(inflater, R.layout.sell_fragment, container, false)
//        initViewModel()
        binding.listener = listener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private val listener: SellFoodClickListener = object : SellFoodClickListener {
        override fun sell() {
            Toast.makeText(context, "Floating Action Bar", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun initViewModel() {
//        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
//        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
//            override fun onChanged(@Nullable foods: List<Food>?) {
//                if (foods != null) {
//                    adapter.setFoodList(foods)
//                }
//
//                binding.executePendingBindings()
//            }
//        })
//    }
}