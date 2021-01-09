package com.suji.android.suji_android.food

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodListAdapter
import com.suji.android.suji_android.base.DataBindingFragmentWithVM
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodFragmentBinding
import com.suji.android.suji_android.dialogs.CreateFoodDialog
import com.suji.android.suji_android.dialogs.ModifyFoodDialog
import kotlinx.android.synthetic.main.create_food_dialog.view.*
import kotlinx.android.synthetic.main.submenu_layout.view.*

class FoodFragment : DataBindingFragmentWithVM<FoodFragmentBinding, FoodViewModel>(R.layout.food_fragment, FoodViewModel::class.java) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = this@FoodFragment
            vm = viewModel
            mainFoodList.adapter = FoodListAdapter(viewModel, ClickHandler())
        }

        lifecycle.addObserver(viewModel)

        initLiveData()
    }

    private fun initLiveData() {
        viewModel.foodList.observe(viewLifecycleOwner) { list ->
            binding.mainFoodList.adapter?.notifyDataSetChanged()
        }
        viewModel.showDialog.observe(viewLifecycleOwner) {
            CreateFoodDialog(
                R.layout.create_food_dialog,
                "음식 추가",
                "추가",
                {
                    val subMenuList = mutableListOf<Food>()
                    val foodName = it.createMenuEditName.text.toString()
                    val foodPrice = it.createMenuEditPrice.text.toString()

                    it.createMenuEditName.text.clear()
                    it.createMenuEditPrice.text.clear()

                    if (foodName.isNullOrBlank() || foodPrice.isNullOrBlank()) {
                        Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                        return@CreateFoodDialog
                    }

                    for (i in 0 until it.createSubMenu.childCount) {
                        val subMenuView = it.createSubMenu.getChildAt(i)
                        val subMenuName = subMenuView.create_submenu_name_edit_text.text.toString()
                        val subMenuPrice = subMenuView.create_submenu_price_edit_text.text.toString()

                        subMenuView.create_submenu_name_edit_text.text.clear()
                        subMenuView.create_submenu_price_edit_text.text.clear()

                        subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                    }

                    if (subMenuList.size == 0) {
                        viewModel.insert(Food(foodName, foodPrice.toInt()))
                    } else {
                        viewModel.insert(Food(foodName, foodPrice.toInt(), subMenuList))
                    }
                },
                "부가메뉴 추가",
                {
                    val subMenuLayout = layoutInflater.inflate(R.layout.submenu_layout, it.createSubMenu, false)
                    subMenuLayout.submenu_delete.setOnClickListener {
                        it.create_sub_menu.removeView(subMenuLayout)
                    }
                    it.createSubMenu.addView(subMenuLayout)
                },
                "취소",
                {
                    it.dismiss()
                }
            ).show(parentFragmentManager, TAG)
        }
    }

    inner class ClickHandler {
        fun modifyFood(item: Food) {
            ModifyFoodDialog(
                R.layout.create_food_dialog,
                item,
                "메뉴 수정",
                "수정",
                {
                    val subMenuList = mutableListOf<Food>()
                    val foodName = it.createMenuEditName.text.toString()
                    val foodPrice = it.createMenuEditPrice.text.toString()

                    if (foodName.isNullOrBlank() || foodPrice.isNullOrBlank()) {
                        Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                        return@ModifyFoodDialog
                    }

                    for (i in 0 until it.createSubMenu.childCount) {
                        val subMenuView = it.createSubMenu.getChildAt(i)
                        val subMenuName = subMenuView.create_submenu_name_edit_text.text.toString()
                        val subMenuPrice = subMenuView.create_submenu_price_edit_text.text.toString()

                        subMenuView.create_submenu_name_edit_text.text.clear()
                        subMenuView.create_submenu_price_edit_text.text.clear()

                        subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                    }

                    item.name = foodName
                    item.price = foodPrice.toInt()
                    item.subMenu = subMenuList

                    viewModel.update(item)
                },
                "부가메뉴 추가",
                {
                    val subMenuLayout = layoutInflater.inflate(R.layout.submenu_layout, it.createSubMenu, false)
                    subMenuLayout.submenu_delete.setOnClickListener {
                        it.create_sub_menu.removeView(subMenuLayout)
                    }
                    it.createSubMenu.addView(subMenuLayout)
                },
                "취소",
                {
                    it.dismiss()
                }
            ).show(parentFragmentManager, TAG)
        }

        fun deleteFood(item: Food) {
            viewModel.delete(item)
        }
    }

    companion object {
        const val TAG = "FoodFragment"
    }
}