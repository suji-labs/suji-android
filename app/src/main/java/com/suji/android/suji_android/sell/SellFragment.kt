package com.suji.android.suji_android.sell

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.SellListAdapter
import com.suji.android.suji_android.base.DataBindingFragmentWithVM
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.SellFragmentBinding
import com.suji.android.suji_android.dialogs.FoodSalesDialog
import com.suji.android.suji_android.dialogs.SalesModifyDialog
import com.suji.android.suji_android.dialogs.SujiDialog
import com.suji.android.suji_android.helper.Constant
import kotlinx.android.synthetic.main.food_sell_dialog.view.*
import kotlinx.android.synthetic.main.sell_fragment.*
import kotlinx.android.synthetic.main.submenu_item.view.*
import java.text.DecimalFormat
import java.time.Instant

class SellFragment : DataBindingFragmentWithVM<SellFragmentBinding, SellViewModel>(R.layout.sell_fragment, SellViewModel::class.java) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SellFragment
            vm = viewModel
            sellFragmentFab.setOnClickListener(floatingButtonClickListener)
            sellFragmentItems.adapter = SellListAdapter(viewModel, ClickHandler())
        }
        lifecycle.addObserver(viewModel)

        initLiveData()
    }

    private fun initLiveData() {
        viewModel.sellList.observe(this) {
            binding.sellFragmentItems.adapter?.notifyDataSetChanged()
        }
    }

    private val floatingButtonClickListener: View.OnClickListener = View.OnClickListener { v ->
            when (v!!.id) {
                R.id.sell_fragment_fab -> {
                    val sale = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        Sale("총 금액", 0, System.currentTimeMillis())
                    } else {
                        Sale("총 금액", 0, Instant.now().toEpochMilli())
                    }

                    FoodSalesDialog(
                        "음식 판매",
                        "판매",
                        {
                            if (sale.totalPrice == 0) {
                                Toast.makeText(context, "음식을 추가하세요!", Toast.LENGTH_SHORT).show()
                            } else {
                                sale.salesTime = System.currentTimeMillis()
                                viewModel.insert(sale)
                            }
                        },
                        "추가",
                        { it, food ->
                            val foodCount = it.sellMainFoodCount.text.toString()

                            if (foodCount.isNullOrBlank()) {
                                Toast.makeText(context, "수량을 확인하세요!", Toast.LENGTH_SHORT).show()
                                return@FoodSalesDialog
                            }

                            if (food.name.isNullOrBlank()) {
                                Toast.makeText(context, "물건을 선택하세요!", Toast.LENGTH_SHORT).show()
                            } else {
                                val item = Food(food.name, food.price, count = foodCount.toInt(), id = food.id)

                                for (i in 0 until food.subMenu.size) {
                                    val subMenuLayout = it.sellSubFoodLayout.getChildAt(i)
                                    var subCount = ""

                                    if (subMenuLayout != null) {
                                        subCount = subMenuLayout.submenu_count_edit_text.text.toString()

                                        subMenuLayout.submenu_count_edit_text.text.clear()
                                    }

                                    if (!subCount.isNullOrBlank()) {
                                        item.subMenu.add(Food(food.subMenu[i].name, food.subMenu[i].price, mutableListOf<Food>(), subCount.toInt(), food.subMenu[i].id))
                                    }
                                }

                                sale.orderedFoods.add(item)
                                sale.totalPrice = viewModel.sumOfPrice(sale)
                                it.foodSaleTotalPrice.text = DecimalFormat.getCurrencyInstance().format(sale.totalPrice).toString()
                                it.sellMainFoodCount.text.clear()
                            }
                        },
                        "취소",
                        {
                            it.dismiss()
                        }
                    ).show(parentFragmentManager, TAG)
                }
            }
        }

    inner class ClickHandler {
        fun sellItem(item: Sale) {
            SujiDialog(
                "결제 방식",
                "현금",
                {
                    item.isSale = true
                    item.pay = Constant.PayType.CASH
                    viewModel.update(item)
                },
                "카드",
                {
                    item.isSale = true
                    item.pay = Constant.PayType.CARD
                    viewModel.update(item)
                }
            ).show(parentFragmentManager, TAG)
        }

        fun modifySaleItem(item: Sale) {
            SalesModifyDialog(
                "음식 수정",
                "적용",
                {
                    viewModel.update(item)
                },
                "추가",
                { binding, food ->
                    val foodCount = binding.sellMainFoodCount.text.toString()
                    val modifyItem = item.orderedFoods.find { item -> item.id == food.id } ?: food

                    if (foodCount.isNullOrBlank()) {
                        Toast.makeText(context, "수량을 확인하세요!", Toast.LENGTH_SHORT).show()
                        return@SalesModifyDialog
                    }

                    if (modifyItem.count + foodCount.toInt() < 0) {
                        Toast.makeText(context, "수량을 확인해주세요!", Toast.LENGTH_SHORT).show()
                        return@SalesModifyDialog
                    } else {
                        modifyItem.count += foodCount.toInt()
                    }

                    for (i in 0 until food.subMenu.size) {
                        binding.sellSubFoodLayout.getChildAt(i)?.let {
                            val subName = it.submenu_name.text.toString()
                            val subCount = it.submenu_count_edit_text.text.toString()
                            val subMenu = modifyItem.subMenu.find { item -> item.name == subName }
                            it.submenu_count_edit_text.text.clear()

                            if (!subCount.isNullOrBlank()) {
                                subMenu?.let {
                                    it.count += subCount.toInt()
                                }
                            }

                            item.totalPrice = viewModel.sumOfPrice(item)
                        }
                    }

                    item.orderedFoods.remove(modifyItem)
                    item.orderedFoods.add(Food(modifyItem.name, modifyItem.price, modifyItem.subMenu, modifyItem.count, modifyItem.id))
                    item.totalPrice = viewModel.sumOfPrice(item)
                    binding.foodSaleTotalPrice.text = DecimalFormat.getCurrencyInstance().format(item.totalPrice).toString()
                    binding.sellMainFoodCount.text.clear()
                },
                "취소",
                {
                    it.dismiss()
                }
            ).show(parentFragmentManager, TAG)
        }

        fun deleteSaleItem(item: Sale) {
            viewModel.delete(item)
        }
    }

    companion object {
        const val TAG = "SellFragment"
    }
}