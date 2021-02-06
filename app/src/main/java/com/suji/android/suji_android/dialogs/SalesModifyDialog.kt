package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.SalesListAdapter
import com.suji.android.suji_android.base.DataBindingDialogWithVM
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodSellDialogBinding
import com.suji.android.suji_android.databinding.SubmenuItemBinding
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.sell.SellViewModel
import kotlinx.android.synthetic.main.food_sell_dialog.view.*
import kotlinx.android.synthetic.main.submenu_item.view.*

class SalesModifyDialog(
    private val message: String,
    private val positiveText: String,
    private val positiveCallback: ((FoodSellDialogBinding) -> Unit)? = null,
    private val neutralText: String,
    private val neutralCallback: ((FoodSellDialogBinding, Food) -> Unit)? = null,
    private val negativeText: String,
    private val negativeCallback: ((Dialog) -> Unit)? = null
) : DataBindingDialogWithVM<FoodSellDialogBinding, FoodSalesDialogViewModel>(R.layout.food_sell_dialog, FoodSalesDialogViewModel::class.java) {
    private val viewModel: FoodSalesDialogViewModel by lazy {
        ViewModelProvider(this).get(FoodSalesDialogViewModel::class.java)
    }
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProvider(this).get(FoodViewModel::class.java)
    }
    private var food = Food()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SalesModifyDialog
            vm = viewModel

            sellItemSpinner.apply {
                adapter = SalesListAdapter(foodViewModel)
                onItemSelectedListener = spinnerItemClick
            }
        }
        lifecycle.addObserver(viewModel)

        viewModel.init(message, positiveText, neutralText, negativeText)

        initLiveData()
    }

    fun initLiveData() {
        viewModel.buttonClicked.observe(this) {
            when (it) {
                0 -> {
                    positiveCallback?.invoke(binding)
                    dismissAllowingStateLoss()
                }
                1 -> { neutralCallback?.invoke(binding, food) }
                2 -> { negativeCallback?.invoke(requireDialog()) }
            }
        }
    }

    private val spinnerItemClick: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val food = parent!!.selectedItem as Food

                if (food.subMenu.size != 0) {
                    for (item in food.subMenu) {
                        item.count = 0
                    }
                }
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val food = parent?.selectedItem as Food

                if (food.subMenu.size != 0) {
                    for (item in food.subMenu) {
                        item.count = 0
                    }
                }

                this@SalesModifyDialog.food = food

                binding.sellSubFoodLayout.removeAllViews()

                for (i in 0 until food.subMenu.size) {
                    val layout = DataBindingUtil.inflate<SubmenuItemBinding>(LayoutInflater.from(context), R.layout.submenu_item, requireView().sell_sub_food_layout, false)
                    layout.submenuName.text = food.subMenu[i].name
                    binding.sellSubFoodLayout.addView(layout.root)
                }
            }
        }
}