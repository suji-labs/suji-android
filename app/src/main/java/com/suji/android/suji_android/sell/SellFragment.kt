package com.suji.android.suji_android.sell

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.system.Os
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodSaleListAdapter
import com.suji.android.suji_android.adapter.SellListAdapter
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import com.suji.android.suji_android.listener.ItemClickListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.food_sell_dialog.view.*
import kotlinx.android.synthetic.main.sell_fragment.view.*
import kotlinx.android.synthetic.main.submenu_item.view.*
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime

class SellFragment : Fragment() {
    private lateinit var adapter: SellListAdapter
    private lateinit var spinnerAdapter: FoodSaleListAdapter
    private var food = Food()
    private val sellViewModel: SellViewModel by lazy {
        ViewModelProvider(this).get(SellViewModel::class.java)
    }
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProvider(this).get(FoodViewModel::class.java)
    }
    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.food_sell_dialog, null, false)
    }
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()

        val view = inflater.inflate(R.layout.sell_fragment, container, false)
        adapter = SellListAdapter(listener)
        view.sell_fragment_fab.setOnClickListener(floatingButtonClickListener)
        view.sell_fragment_items.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.sell_fragment_items.adapter = adapter

        dialogView.sell_item_spinner.apply {
            spinnerAdapter = FoodSaleListAdapter()
            adapter = spinnerAdapter
            onItemSelectedListener = spinnerItemClick
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        sellViewModel.getAllSale()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { adapter.setItems(it) },
                { e -> e.printStackTrace() }
            ).addTo(disposeBag)

        foodViewModel.getAllFood()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { spinnerAdapter.setItems(it) },
                { e -> e.printStackTrace() }
            ).addTo(disposeBag)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
    }

    private val spinnerItemClick: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val food = parent!!.selectedItem as Food

                if (food.sub.size != 0) {
                    for (item in food.sub) {
                        item.count = 0
                    }
                }
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val food = parent!!.selectedItem as Food

                if (food.sub.size != 0) {
                    for (item in food.sub) {
                        item.count = 0
                    }
                }

                this@SellFragment.food = food

                dialogView.sell_sub_food_layout.removeAllViews()

                for (i in 0 until food.sub.size) {
                    val layout = LayoutInflater.from(context)
                        .inflate(R.layout.submenu_item, dialogView.sell_sub_food_layout, false)
                    layout.submenu_name.text = food.sub[i].name
                    dialogView.sell_sub_food_layout.addView(layout)
                }
            }
        }

    private val floatingButtonClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.sell_fragment_fab -> {

                    val sale = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        Sale("총 금액", 0, System.currentTimeMillis())
                    } else {
                        Sale("총 금액", 0, Instant.now().toEpochMilli())
                    }

                    if (dialogView.sell_item_spinner.count == 0) {
                        Toast.makeText(context, "등록된 음식이 없습니다!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    dialogView.sell_item_spinner.setSelection(0)
                    dialogView.food_sale_total_price.text = "0"

                    AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
                        .setPositiveButton("판매") { dialog, which ->
                            if (sale.price == 0) {
                                Toast.makeText(context, "음식을 추가하세요!", Toast.LENGTH_SHORT).show()
                            } else {
                                sale.time = System.currentTimeMillis()
                                sellViewModel.insert(sale)

                                dialog!!.dismiss()
                            }

                            (dialogView.parent as ViewGroup).removeView(dialogView)
                        }
                        .setNegativeButton("취소") { dialog, which ->
                            (dialogView.parent as ViewGroup).removeView(dialogView)
                            dialog!!.dismiss()
                        }
                        .setNeutralButton("추가", null)
                        .setOnCancelListener {
                            (dialogView.parent as ViewGroup).removeView(dialogView)
                        }
                        .setView(dialogView)
                        .show().let {
                            it.getButton(AlertDialog.BUTTON_NEUTRAL)
                                .setOnClickListener { view ->
                                    val foodCount = dialogView.sell_main_food_count.text.toString()

                                    if (foodCount.isNullOrBlank()) {
                                        Toast.makeText(context, "수량을 확인하세요!", Toast.LENGTH_SHORT).show()
                                        return@setOnClickListener
                                    }

                                    if (food.name.isNullOrBlank()) {
                                        Toast.makeText(context, "물건을 선택하세요!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Observable.range(0, food.sub.size + 1)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map { subMenuPosition ->
                                                val subMenuLayout = dialogView.sell_sub_food_layout.getChildAt(subMenuPosition)
                                                var subCount = ""

                                                if (subMenuLayout != null) {
                                                    subCount =
                                                        subMenuLayout.submenu_count_edit_text.text.toString()

                                                    subMenuLayout.submenu_count_edit_text.text.clear()
                                                }

                                                if (!subCount.isNullOrBlank()) {
                                                    food.sub[subMenuPosition].count = subCount.toInt()
                                                }

                                                return@map food
                                            }.subscribe(
                                                { result ->
                                                    sale.foods.add(
                                                        Food(
                                                            result.name,
                                                            result.price,
                                                            result.sub,
                                                            foodCount.toInt(),
                                                            result.id
                                                        )
                                                    )

                                                    sale.price = sellViewModel.sumOfPrice(sale)
                                                },
                                                { e -> e.printStackTrace() },
                                                {
                                                    dialogView.food_sale_total_price.text =
                                                        DecimalFormat.getCurrencyInstance().format(sale.price)
                                                            .toString()
                                                    dialogView.sell_main_food_count.text.clear()
                                                }
                                            ).addTo(disposeBag)
                                    }
                                }
                            Utils.dialogReSizing(it)
                        }
                }
            }
        }
    }

    private val listener = object : ItemClickListener {
        override fun onItemClick(view: View, item: Any?) {
            when (view.id) {
                R.id.sell_item_sold -> {
                    AlertDialog.Builder(context)
                        .setTitle("결제 방식을 선택하세요")
                        .setPositiveButton("현금") { dialog, which ->
                            if (item is Sale) {
                                item.isSale = true
                                item.pay = Constant.PayType.CASH
                                sellViewModel.update(item)
                            }
                        }
                        .setNegativeButton("카드") { dialog, which ->
                            if (item is Sale) {
                                item.isSale = true
                                item.pay = Constant.PayType.CARD
                                sellViewModel.update(item)
                            }
                        }
                        .setNeutralButton("취소") { dialog, which -> dialog!!.dismiss() }
                        .show()
                }
                R.id.sell_item_modify -> {
                    dialogView.sell_item_spinner.setSelection(0)

                    if (item is Sale) {
                        val tempItem = item.copy()
                        dialogView.food_sale_total_price.text =
                            DecimalFormat.getCurrencyInstance().format(item.price).toString()

                        AlertDialog.Builder(context, R.style.AppTheme_AppCompat_CustomDialog)
                            .setPositiveButton("적용") { dialog, which ->
                                for (i in tempItem.foods) {
                                    item.foods.add(i)
                                }
                                sellViewModel.update(item)
                                (dialogView.parent as ViewGroup).removeView(dialogView)

                                dialog!!.dismiss()
                            }
                            .setNegativeButton("취소") { dialog, which ->
                                (dialogView.parent as ViewGroup).removeView(dialogView)

                                dialog!!.dismiss()
                            }
                            .setOnCancelListener {
                                (dialogView.parent as ViewGroup).removeView(dialogView)
                            }
                            .setNeutralButton("추가", null)
                            .setView(dialogView)
                            .show().let {
                                it.getButton(AlertDialog.BUTTON_NEUTRAL)
                                    .setOnClickListener { view ->
                                        val foodCount =
                                            dialogView.sell_main_food_count.text.toString()
                                        var modifyItem: Food? =
                                            item.foods.find { item -> item.id == food.id }

                                        if (foodCount.isNullOrBlank()) {
                                            Toast.makeText(
                                                context,
                                                "수량을 확인하세요!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@setOnClickListener
                                        }

                                        if (modifyItem == null) {
                                            modifyItem = food
                                        }

                                        if (modifyItem.count + foodCount.toInt() < 0) {
                                            Toast.makeText(
                                                context,
                                                "수량을 확인해주세요!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@setOnClickListener
                                        } else {
                                            modifyItem.count += foodCount.toInt()
                                        }

                                        Observable.range(0, food.sub.size + 1)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map { subMenuPosition ->
                                                val subMenuLayout =
                                                    dialogView.sell_sub_food_layout.getChildAt(
                                                        subMenuPosition
                                                    )
                                                var subCount = ""
                                                var subMenu: Food? = Food()

                                                if (subMenuLayout != null) {
                                                    val subName =
                                                        subMenuLayout.submenu_name.text.toString()
                                                    subCount =
                                                        subMenuLayout.submenu_count_edit_text.text.toString()
                                                    subMenu =
                                                        modifyItem.sub.find { item -> item.name == subName }
                                                    subMenuLayout.submenu_count_edit_text.text.clear()
                                                }

                                                if (!subCount.isNullOrBlank()) {
                                                    if (subMenu != null) {
                                                        subMenu.count += subCount.toInt()
                                                    }
                                                }

                                                tempItem.price = sellViewModel.sumOfPrice(tempItem)

                                                return@map tempItem
                                            }
                                            .subscribe(
                                                { result ->
                                                    result.foods.add(
                                                        Food(
                                                            modifyItem.name,
                                                            modifyItem.price,
                                                            modifyItem.sub,
                                                            modifyItem.count,
                                                            modifyItem.id
                                                        )
                                                    )
                                                },
                                                { e -> e.printStackTrace() },
                                                {
                                                    dialogView.food_sale_total_price.text =
                                                        DecimalFormat.getCurrencyInstance()
                                                            .format(tempItem.price).toString()
                                                    dialogView.sell_main_food_count.text.clear()
                                                }
                                            )
                                            .addTo(disposeBag)
                                    }

                                Utils.dialogReSizing(it)
                            }
                    }
                }
                R.id.sell_item_delete -> {
                    if (item is Sale) {
                        sellViewModel.delete(item)
                    }
                }
            }
        }
    }
}