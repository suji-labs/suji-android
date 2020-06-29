package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.Utils
import kotlinx.android.synthetic.main.food_create_dialog.view.*
import kotlinx.android.synthetic.main.submenu_layout.view.*

class ModifyFoodDialogFragment : DialogFragment() {
    private lateinit var builder: AlertDialog.Builder
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProvider(this).get(FoodViewModel::class.java)
    }
    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.food_create_dialog, null, false)
    }
    private val subMenuLayout: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.submenu_layout, dialogView.create_sub_menu, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val item = arguments?.get("food") as Food

        dialogView.create_menu_edit_name.setText(item.name)
        dialogView.create_menu_edit_price.setText(item.price.toString())

        for (subItem in item.subMenu) {
            subMenuLayout.submenu_delete.setOnClickListener {
                dialogView.create_sub_menu.removeView(subMenuLayout)
            }
            subMenuLayout.create_submenu_name_edit_text.setText(subItem.name)
            subMenuLayout.create_submenu_price_edit_text.setText(subItem.price.toString())
            dialogView.create_sub_menu.addView(subMenuLayout)
        }

        builder = AlertDialog.Builder(requireActivity(), R.style.AppTheme_AppCompat_CustomDialog)
            .setPositiveButton("수정") { dialogInterface, which ->
                val subMenuList: ArrayList<Food> = ArrayList()
                val foodName: String = dialogView.create_menu_edit_name.text.toString()
                val foodPrice: String = dialogView.create_menu_edit_price.text.toString()

                if (foodName.isNullOrBlank() || foodPrice.isNullOrBlank()) {
                    Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                for (i in 0 until dialogView.create_sub_menu.childCount) {
                    val subMenuView = dialogView.create_sub_menu.getChildAt(i)
                    val subMenuName = subMenuView.create_submenu_name_edit_text.text.toString()
                    val subMenuPrice = subMenuView.create_submenu_price_edit_text.text.toString()
                    Log.i("subItem", subMenuName)

                    subMenuView.create_submenu_name_edit_text.text.clear()
                    subMenuView.create_submenu_price_edit_text.text.clear()

                    subMenuList.add(Food(foodName, foodPrice.toInt()))
                }

                item.name = foodName
                item.price = foodPrice.toInt()

                if (item.subMenu.size != 0) {
                    item.subMenu = subMenuList
                }

                foodViewModel.update(item)

                (dialogView.parent as ViewGroup).removeAllViews()
                (dialogView.create_sub_menu as ViewGroup).removeAllViews()
                dialog!!.dismiss()
            }
            .setNegativeButton("취소") { dialogInterface, which ->
                (dialogView.parent as ViewGroup).removeAllViews()
                (dialogView.create_sub_menu as ViewGroup).removeAllViews()
                dialog!!.dismiss()
            }
            .setNeutralButton("부가 메뉴", null)
            .setOnCancelListener {
                (dialogView.parent as ViewGroup).removeView(dialogView)
                (dialogView.create_sub_menu as ViewGroup).removeAllViews()
            }
            .setView(dialogView)

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL)
                .setOnClickListener { view ->
                    val subMenuLayout = layoutInflater.inflate(R.layout.submenu_layout, dialogView.create_sub_menu, false)
                    subMenuLayout.submenu_delete.setOnClickListener {
                        dialogView.create_sub_menu.removeView(subMenuLayout)
                    }
                    dialogView.create_sub_menu.addView(subMenuLayout)
                }
        }

        return dialog
    }

    override fun onResume() {
        super.onResume()
        Utils.dialogReSizing(dialog!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (dialogView.parent != null) {
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }

        if (subMenuLayout.parent != null) {
            (subMenuLayout.parent as ViewGroup).removeView(subMenuLayout)
        }
    }

    companion object {
        private val INSTANCE = ModifyFoodDialogFragment()

        fun newInstance(): ModifyFoodDialogFragment {
            return INSTANCE
        }

        fun newInstance(food: Food): ModifyFoodDialogFragment {
            val args = Bundle().apply {
                putParcelable("food", food)
            }
            INSTANCE.arguments = args
            return INSTANCE
        }
    }
}