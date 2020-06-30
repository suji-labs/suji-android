package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
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

class CreateFoodDialogFragment : DialogFragment() {
    private lateinit var builder: AlertDialog.Builder
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProvider(this).get(FoodViewModel::class.java)
    }
    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.food_create_dialog, null, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(requireActivity(), R.style.AppTheme_AppCompat_CustomDialog)
            .setPositiveButton("만들기") { dialogInterface, which ->
                val subMenuList: ArrayList<Food> = ArrayList()
                val foodName: String = dialogView.create_menu_edit_name.text.toString()
                val foodPrice: String = dialogView.create_menu_edit_price.text.toString()

                dialogView.create_menu_edit_name.text.clear()
                dialogView.create_menu_edit_price.text.clear()

                if (foodName.isNullOrBlank() || foodPrice.isNullOrBlank()) {
                    Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                for (i in 0 until dialogView.create_sub_menu.childCount) {
                    val subMenuView = dialogView.create_sub_menu.getChildAt(i)
                    val subMenuName = subMenuView.create_submenu_name_edit_text.text.toString()
                    val subMenuPrice = subMenuView.create_submenu_price_edit_text.text.toString()

                    subMenuView.create_submenu_name_edit_text.text.clear()
                    subMenuView.create_submenu_price_edit_text.text.clear()

                    subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                }

                if (subMenuList.size == 0) {
                    foodViewModel.insert(Food(foodName, foodPrice.toInt()))
                } else {
                    foodViewModel.insert(Food(foodName, foodPrice.toInt(), subMenuList))
                }

                (dialogView.parent as ViewGroup).removeAllViews()
                dialog!!.dismiss()
            }
            .setNegativeButton("취소") { dialogInterface, which ->
                (dialogView.parent as ViewGroup).removeAllViews()
                (dialogView.create_sub_menu as ViewGroup).removeAllViews()
                dialog!!.dismiss()
            }
            .setNeutralButton("부가 메뉴", null)
            .setOnCancelListener { (dialogView.parent as ViewGroup).removeView(dialogView) }
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
        Utils.dialogReSizing(requireDialog())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (dialogView.parent != null) {
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }
    }

    companion object {
        private val INSTANCE = CreateFoodDialogFragment()

        fun newInstance(): CreateFoodDialogFragment {
            return INSTANCE
        }
    }
}