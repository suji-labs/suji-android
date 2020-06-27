package com.suji.android.suji_android.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.SalesListAdapter
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.Utils
import com.suji.android.suji_android.sell.SellViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.food_sell_dialog.view.*
import kotlinx.android.synthetic.main.submenu_item.view.*
import java.text.DecimalFormat

class FoodSalesDialogFragment : DialogFragment() {
    private lateinit var builder: AlertDialog.Builder
    private var food = Food()
    private val spinnerAdapter = SalesListAdapter()
    private val disposeBag = CompositeDisposable()
    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.food_sell_dialog, null, false)
    }
    private val sellViewModel: SellViewModel by lazy {
        ViewModelProvider(this).get(SellViewModel::class.java)
    }
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProvider(this).get(FoodViewModel::class.java)
    }

    private fun initView() {
        foodViewModel.getAllFood()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { spinnerAdapter.setItems(it) },
                { e -> e.printStackTrace() }
            ).addTo(disposeBag)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()

        dialogView.sell_item_spinner.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = spinnerItemClick
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sale = requireArguments().get("sale") as Sale

        dialogView.food_sale_total_price.text = "0"
        builder = AlertDialog.Builder(activity!!, R.style.AppTheme_AppCompat_CustomDialog)
            .setPositiveButton("판매") { dialogInterface, i ->
                if (sale.totalPrice == 0) {
                    Toast.makeText(context, "음식을 추가하세요!", Toast.LENGTH_SHORT).show()
                } else {
                    sale.salesTime = System.currentTimeMillis()
                    sellViewModel.insert(sale)

                    dialogInterface.dismiss()
                }

                (dialogView.parent as ViewGroup).removeView(dialogView)
            }
            .setNegativeButton("취소") { dialogInterface, i ->
                (dialogView.parent as ViewGroup).removeView(dialogView)
                dialogInterface.dismiss()
            }
            .setNeutralButton("추가", null)
            .setOnCancelListener { dialogInterface ->
                (dialogView.parent as ViewGroup).removeView(dialogView)
                dialogInterface.dismiss()
            }
            .setView(dialogView)

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                .setOnClickListener {
                    val foodCount = dialogView.sell_main_food_count.text.toString()

                    if (foodCount.isNullOrBlank()) {
                        Toast.makeText(context, "수량을 확인하세요!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (food.name.isNullOrBlank()) {
                        Toast.makeText(context, "물건을 선택하세요!", Toast.LENGTH_SHORT).show()
                    } else {
                        val item = Food(food.name, food.price, count = foodCount.toInt(), id = food.id)

                        for (i in 0 until food.subMenu.size) {
                            val subMenuLayout = dialogView.sell_sub_food_layout.getChildAt(i)
                            var subCount = ""

                            if (subMenuLayout != null) {
                                subCount = subMenuLayout.submenu_count_edit_text.text.toString()

                                subMenuLayout.submenu_count_edit_text.text.clear()
                            }

                            if (!subCount.isNullOrBlank()) {
                                item.subMenu.add(Food(food.subMenu[i].name, food.subMenu[i].price, ArrayList<Food>(), subCount.toInt(), food.subMenu[i].id))
                            }
                        }

                        sale.orderedFoods.add(item)
                        sale.totalPrice = sellViewModel.sumOfPrice(sale)
                        dialogView.food_sale_total_price.text = DecimalFormat.getCurrencyInstance().format(sale.totalPrice).toString()
                        dialogView.sell_main_food_count.text.clear()
                    }
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
        (dialogView as ViewGroup).removeView(dialogView)
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
                val food = parent!!.selectedItem as Food

                if (food.subMenu.size != 0) {
                    for (item in food.subMenu) {
                        item.count = 0
                    }
                }

                this@FoodSalesDialogFragment.food = food

                dialogView.sell_sub_food_layout.removeAllViews()

                for (i in 0 until food.subMenu.size) {
                    val layout = LayoutInflater.from(context)
                        .inflate(R.layout.submenu_item, dialogView.sell_sub_food_layout, false)
                    layout.submenu_name.text = food.subMenu[i].name
                    dialogView.sell_sub_food_layout.addView(layout)
                }
            }
        }

    companion object {
        private val INSTANCE =
            FoodSalesDialogFragment()

        fun newInstance(): FoodSalesDialogFragment {
            return INSTANCE
        }

        fun newInstance(sale: Sale): FoodSalesDialogFragment {
            val args = Bundle().apply {
                putParcelable("sale", sale)
            }
            INSTANCE.arguments = args
            return INSTANCE
        }
    }
}