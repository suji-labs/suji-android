package com.suji.android.suji_android.sell

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodSaleListAdapter
import com.suji.android.suji_android.adapter.ProductListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.FoodSellDialogBinding
import com.suji.android.suji_android.databinding.SellFragmentBinding
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Utils
import com.suji.android.suji_android.listener.ItemClickListener
import java.text.DecimalFormat

class SellFragment : Fragment() {
    private lateinit var binding: SellFragmentBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var spinnerAdapter: FoodSaleListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var sellViewModel: SellViewModel = SellViewModel(BasicApp.app)
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)
    private lateinit var dialogBinding: FoodSellDialogBinding
    private var food: Food? = null
    private val subMenuPriceID = 0x6000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModel()
        binding = DataBindingUtil.inflate<SellFragmentBinding>(
            inflater,
            R.layout.sell_fragment,
            container,
            false
        )
            .apply {
                adapter = ProductListAdapter(Constant.ViewType.SALE_VIEW)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                listener = floatingButtonClickListener
                sellFragmentItems.layoutManager = layoutManager
                sellFragmentItems.adapter = adapter
            }

        dialogBinding = DataBindingUtil.inflate<FoodSellDialogBinding>(
            inflater,
            R.layout.food_sell_dialog,
            null,
            false
        )

        dialogBinding.sellItemSpinner.apply {
            spinnerAdapter = FoodSaleListAdapter()
            adapter = spinnerAdapter
            onItemSelectedListener = spinnerItemClick
        }
        Constant.ListenerHashMap.listenerList["foodSellClickListener"] = foodSellClickListener
        Constant.ListenerHashMap.listenerList["addSaleClickListener"] = addSaleClickListener
        Constant.ListenerHashMap.listenerList["foodSaleCancelClickListener"] = foodSaleCancelClickListener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

            val linearLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val labelWeight = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                .apply {
                    weight = 1f
                }
            val editWeight = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                .apply {
                    weight = 2f
                }

            dialogBinding.sellSubFoodLayout.removeAllViews()

            for (i in 0 until food.sub.size) {
                val layout = LinearLayout(context).apply {
                    layoutParams = linearLayoutParams
                    orientation = LinearLayout.HORIZONTAL
                }

                val label = BootstrapLabel(context).apply {
                    bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                    text = food.sub[i].name
                }

                val edit = BootstrapEditText(context).apply {
                    setId(subMenuPriceID + i)
                    setTextColor(Color.BLACK)
                    inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
                }

                layout.addView(label, labelWeight)
                layout.addView(edit, editWeight)
                dialogBinding.sellSubFoodLayout.addView(layout)
            }

            executePendingBindings()
        }
    }

    private val foodSellClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
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

            executePendingBindings()
        }
    }

    private val floatingButtonClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            val sale = Sale("총 금액", 0, System.currentTimeMillis())

            if (dialogBinding.sellItemSpinner.count == 0) {
                Toast.makeText(context, "등록된 음식이 없습니다!", Toast.LENGTH_SHORT).show()
                return
            }

            dialogBinding.sellItemSpinner.setSelection(0)
            dialogBinding.foodSaleTotalPrice.text = "0"

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

                        (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
                    }
                })
                .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
                        dialog!!.dismiss()
                    }
                })
                .setNeutralButton("추가", null)
                .setView(dialogBinding.root)
                .show().let {
                    it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            val foodCountString = dialogBinding.sellMainFoodCount.text.toString()
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

                            dialogBinding.foodSaleTotalPrice.text =
                                DecimalFormat.getCurrencyInstance().format(sale.price).toString()
                            dialogBinding.sellMainFoodCount.setText("")
                        }
                    })

                    Utils.dialogReSizing(it)
                }

            executePendingBindings()
        }
    }

    private val addSaleClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            dialogBinding.sellItemSpinner.setSelection(0)

            if (item is Sale) {
                dialogBinding.foodSaleTotalPrice.text =
                    DecimalFormat.getCurrencyInstance().format(item.price).toString()

                AlertDialog.Builder(context, R.style.AppTheme_AppCompat_CustomDialog)
                    .setPositiveButton("적용", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            sellViewModel.update(item)
                            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)

                            dialog!!.dismiss()
                        }
                    })
                    .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)

                            dialog!!.dismiss()
                        }
                    })
                    .setNeutralButton("추가", null)
                    .setView(dialogBinding.root)
                    .show().let {
                        it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val foodCountString = dialogBinding.sellMainFoodCount.text.toString()
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

                                dialogBinding.foodSaleTotalPrice.text =
                                    DecimalFormat.getCurrencyInstance().format(item.price).toString()
                                dialogBinding.sellMainFoodCount.setText("")
                            }
                        })

                        Utils.dialogReSizing(it)
                    }
            }

            executePendingBindings()
        }
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
                if (!Utils.nullCheck(dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i))) {
                    if (Utils.blankString(dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString())) {
                        continue
                    }

                    subFoodCount = dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i)
                        .text
                        .toString()
                        .toInt()

                    dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i).setText("")
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

        if (!Utils.nullCheck(dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID))) {
            for (i in 0 until food!!.sub.size) {
                dialogBinding.root.findViewById<BootstrapEditText>(subMenuPriceID + i).setText("")
            }
        }

        return mainSumPrice + subSumPrice
    }

    private val foodSaleCancelClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            if (item is Sale) {
                sellViewModel.delete(item)
            }

            executePendingBindings()
        }
    }

    private fun initViewModel() {
        sellViewModel = ViewModelProviders.of(this).get(SellViewModel::class.java)
        sellViewModel.getAllSale().observe(this, object : Observer<List<Sale>> {
            override fun onChanged(@Nullable sales: List<Sale>?) {
                sales?.let {
                    adapter.setItems(sales)
                }

                executePendingBindings()
            }
        })

        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                foods?.let {
                    spinnerAdapter.setItems(it)
                }

                executePendingBindings()
            }
        })
    }

    private fun executePendingBindings() {
        binding.executePendingBindings()
    }
}