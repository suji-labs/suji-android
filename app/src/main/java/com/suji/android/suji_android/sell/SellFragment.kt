package com.suji.android.suji_android.sell

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodSaleListAdapter
import com.suji.android.suji_android.adapter.SellListAdapter
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import com.suji.android.suji_android.listener.ItemClickListener
import kotlinx.android.synthetic.main.food_sell_dialog.view.*
import kotlinx.android.synthetic.main.sell_fragment.view.*
import kotlinx.android.synthetic.main.submenu_item.view.*
import java.text.DecimalFormat

class SellFragment : Fragment() {
    private lateinit var adapter: SellListAdapter
    private lateinit var spinnerAdapter: FoodSaleListAdapter
    private var food: Food? = null
    private val subMenuPriceID = 0x6000
    private val sellViewModel: SellViewModel by lazy {
        ViewModelProviders.of(this).get(SellViewModel::class.java)
    }
    private val foodViewModel: FoodViewModel by lazy {
        ViewModelProviders.of(this).get(FoodViewModel::class.java)
    }
    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.food_sell_dialog, null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModel()

        val view = inflater.inflate(R.layout.sell_fragment, container, false)
        adapter = SellListAdapter(listener)
        view.sell_fragment_fab.setOnClickListener(floatingButtonClickListener)
        view.sell_fragment_items.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
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

    private fun sumOfPrice(sale: Sale): Int {
        var subFoodCount = 0
        var subSumPrice = 0
        var mainSumPrice = 0

        sale.foods.iterator().let { iter ->
            while (iter.hasNext()) {
                val f = iter.next()
                mainSumPrice += f.price * f.count
            }
        }

        for (item in sale.foods) {
            if (item.sub.size == 0) {
                continue
            }

            for (i in 0 until item.sub.size) {
                if (!Utils.nullCheck(dialogView.findViewById<BootstrapEditText>(subMenuPriceID + i))) {
                    if (Utils.blankString(dialogView.findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString())) {
                        continue
                    }

                    subFoodCount = dialogView.findViewById<BootstrapEditText>(subMenuPriceID + i)
                        .text
                        .toString()
                        .toInt()

                    dialogView.findViewById<BootstrapEditText>(subMenuPriceID + i).text.clear()
                }

                sale.foods.find { it.name == item.name }?.let {
                    it.sub.find { it.name == item.sub[i].name }?.let {
                        it.count += subFoodCount
                        item.sub[i].count = it.count
                    }
                }

                subSumPrice += item.sub[i].price * (item.sub[i].count)
            }
        }

        if (!Utils.nullCheck(dialogView.findViewById<BootstrapEditText>(subMenuPriceID))) {
            for (i in 0 until food!!.sub.size) {
                dialogView.findViewById<BootstrapEditText>(subMenuPriceID + i).text.clear()
            }
        }

        return mainSumPrice + subSumPrice
    }

    private fun initViewModel() {
        sellViewModel.getAllSale().observe(this, object : Observer<List<Sale>> {
            override fun onChanged(@Nullable sales: List<Sale>?) {
                sales?.let {
                    adapter.setItems(sales)
                }
            }
        })

        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                foods?.let {
                    spinnerAdapter.setItems(it)
                }
            }
        })
    }

    private val spinnerItemClick: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                val layout = LayoutInflater.from(context).inflate(R.layout.submenu_item, dialogView.sell_sub_food_layout, false)
                layout.submenu_name.text = food.sub[i].name
                dialogView.sell_sub_food_layout.addView(layout)
            }
        }
    }

    private val floatingButtonClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.sell_fragment_fab -> {
                    val sale = Sale("총 금액", 0, System.currentTimeMillis())

                    if (dialogView.sell_item_spinner.count == 0) {
                        Toast.makeText(context, "등록된 음식이 없습니다!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    dialogView.sell_item_spinner.setSelection(0)
                    dialogView.food_sale_total_price.text = "0"

                    AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
                        .setPositiveButton("판매", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                if (sale.price == 0) {
                                    Toast.makeText(context, "음식을 추가하세요!", Toast.LENGTH_SHORT).show()
                                } else {
                                    sale.time = System.currentTimeMillis()
                                    sellViewModel.insert(sale)

                                    dialog!!.dismiss()
                                }

                                (dialogView.parent as ViewGroup).removeView(dialogView)
                            }
                        })
                        .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                (dialogView.parent as ViewGroup).removeView(dialogView)
                                dialog!!.dismiss()
                            }
                        })
                        .setNeutralButton("추가", null)
                        .setView(dialogView)
                        .show().let {
                            it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    val foodCountString = dialogView.sell_main_food_count.text.toString()
                                    var item: Food? = sale.foods.find { it.name == food!!.name }

                                    if (Utils.blankString(foodCountString)) {
                                        Toast.makeText(context, "수량을 확인하세요!", Toast.LENGTH_SHORT).show()
                                        return
                                    }

                                    if (Utils.nullCheck(item)) {
                                        item = food
                                    } else {
                                        sale.foods.remove(item)
                                    }

                                    sale.foods.add(Food(item!!.name, item.price, item.sub, item.count + foodCountString.toInt()))

                                    sale.price = sumOfPrice(sale)

                                    dialogView.food_sale_total_price.text =
                                        DecimalFormat.getCurrencyInstance().format(sale.price).toString()
                                    dialogView.sell_main_food_count.text.clear()
                                }
                            })

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
                        .setPositiveButton("현금", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                if (item is Sale) {
                                    item.sell = true
                                    item.pay = Constant.PayType.CASH
                                    sellViewModel.update(item)
                                }
                            }
                        })
                        .setNegativeButton("카드", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                if (item is Sale) {
                                    item.sell = true
                                    item.pay = Constant.PayType.CARD
                                    sellViewModel.update(item)
                                }
                            }
                        })
                        .setNeutralButton("취소", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                            }
                        })
                        .show()
                }
                R.id.sell_item_modify -> {
                    dialogView.sell_item_spinner.setSelection(0)

                    if (item is Sale) {
                        dialogView.food_sale_total_price.text =
                            DecimalFormat.getCurrencyInstance().format(item.price).toString()

                        AlertDialog.Builder(context, R.style.AppTheme_AppCompat_CustomDialog)
                            .setPositiveButton("적용", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    sellViewModel.update(item)
                                    (dialogView.parent as ViewGroup).removeView(dialogView)

                                    dialog!!.dismiss()
                                }
                            })
                            .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    (dialogView.parent as ViewGroup).removeView(dialogView)

                                    dialog!!.dismiss()
                                }
                            })
                            .setNeutralButton("추가", null)
                            .setView(dialogView)
                            .show().let {
                                it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        val foodCountString = dialogView.sell_main_food_count.text.toString()
                                        val removeItem: Food? = item.foods.find { it.name == food!!.name }
                                        val foodCount: Int

                                        if (Utils.blankString(foodCountString)) {
                                            Toast.makeText(context, "수량을 확인하세요!", Toast.LENGTH_SHORT).show()
                                            return
                                        }

                                        foodCount = foodCountString.toInt()

                                        if (Utils.nullCheck(removeItem)) {
                                            if (foodCount < 0) {
                                                Toast.makeText(context, "주문되지 않은 음식은 뺄 수 없습니다!", Toast.LENGTH_SHORT).show()
                                                return
                                            }
                                            item.foods.add(Food(food!!.name, food!!.price, food!!.sub, food!!.count + foodCount))
                                        } else {
                                            if (foodCount < 0 && removeItem!!.count + foodCount < 0) {
                                                Toast.makeText(context, "수량을 확인해주세요!", Toast.LENGTH_SHORT).show()
                                                return
                                            }
                                            if (foodCount + removeItem!!.count > 0) {
                                                item.foods.add(Food(removeItem.name, removeItem.price, removeItem.sub, removeItem.count + foodCount))
                                            }
                                            item.foods.remove(removeItem)
                                        }

                                        item.price = sumOfPrice(item)

                                        dialogView.food_sale_total_price.text =
                                            DecimalFormat.getCurrencyInstance().format(item.price).toString()
                                        dialogView.sell_main_food_count.text.clear()
                                    }
                                })

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