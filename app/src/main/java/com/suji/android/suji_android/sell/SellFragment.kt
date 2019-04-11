package com.suji.android.suji_android.sell

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.suji.android.suji_android.databinding.SellFragmentBinding
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.DisplayHelper
import com.suji.android.suji_android.listener.ItemClickListener
import org.joda.time.DateTime
import java.text.DecimalFormat

class SellFragment : Fragment() {
    private lateinit var binding: SellFragmentBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var sellViewModel: SellViewModel = SellViewModel(BasicApp.app)
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)
    private lateinit var foods: List<Food>
    private lateinit var inflater: LayoutInflater
    private lateinit var foodSaleView: View
    private var food: Food? = null
    private var sale: Sale? = null
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
        Constant.ListenerHashMap.listenerList["foodSellClickListener"] = foodSellClickListener
        Constant.ListenerHashMap.listenerList["addSaleClickListener"] = addSaleClickListener
        Constant.ListenerHashMap.listenerList["foodSaleCancelClickListener"] = foodSaleCancelClickListener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflater = activity!!.layoutInflater
        foodSaleView = inflater.inflate(R.layout.food_sell_dialog, null)
    }

    private val floatingButtonClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            foodSaleView.findViewById<Spinner>(R.id.sell_item_spinner).adapter = FoodSaleListAdapter(foods)
            foodSaleView.findViewById<Spinner>(R.id.sell_item_spinner).onItemSelectedListener = spinnerItemClick
            foodSaleView.findViewById<TextView>(R.id.food_sale_total_price).text = "0"

            AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
                .setPositiveButton("판매", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (sale == null) {
                            Toast.makeText(context, "음식을 추가하세요!", Toast.LENGTH_SHORT).show()
                        } else {
                            sellViewModel.insert(sale!!)

                            foodSaleView.findViewById<TextView>(R.id.food_sale_total_price).text = "0"
                            sale = null

                            dialog!!.dismiss()
                        }

                        (foodSaleView.parent as ViewGroup).removeView(foodSaleView)
                    }
                })
                .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        foodSaleView.findViewById<TextView>(R.id.food_sale_total_price).text = "0"
                        sale = null
                        (foodSaleView.parent as ViewGroup).removeView(foodSaleView)
                        dialog!!.dismiss()
                    }
                })
                .setNeutralButton("추가", null)
                .setView(foodSaleView)
                .show().let {
                    it.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            var sumPrice = 0
                            var foodCount: Int
                            val temp: Food?

                            foodSaleView.findViewById<TextView>(R.id.food_sale_total_price).text = "0"

                            if (sale == null) {
                                sale = Sale("총 금액", sumPrice, DateTime(), HashSet<Food>())
                            }

                            foodCount = foodSaleView.findViewById<BootstrapEditText>(R.id.sell_main_food_count)
                                .text
                                .toString()
                                .toInt()

                            sale!!.foods.find { it.name == food!!.name }?.let {
                                temp = sale!!.foods.find { it.name == food!!.name }
                                sale!!.foods.remove(temp)
                                sale!!.foods.add(Food(temp?.name!!, temp.price, temp.sub, temp.count + foodCount))
                            }.let {
                                sale!!.foods.add(Food(food!!.name, food!!.price, food!!.sub, food!!.count + foodCount))
                            }


                            sale!!.foods.iterator().let { iter ->
                                while (iter.hasNext()) {
                                    val f = iter.next()
                                    sumPrice += f.price * f.count
                                }
                            }

                            foodSaleView.findViewById<BootstrapEditText>(R.id.sell_main_food_count).setText("")

                            for (i in 0 until food!!.sub.size) {
                                if (foodSaleView.findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString() == "") {
                                    continue
                                }
                                foodCount = foodSaleView.findViewById<BootstrapEditText>(subMenuPriceID + i)
                                    .text
                                    .toString()
                                    .toInt()

                                sale!!.foods.add(
                                    Food(
                                        food!!.sub[i].name,
                                        food!!.sub[i].price,
                                        food!!.sub[i].sub,
                                        food!!.sub[i].count + foodCount
                                    )
                                )

                                sumPrice += food!!.sub[i].price * (food!!.sub[i].count + foodCount)

                                foodSaleView.findViewById<BootstrapEditText>(subMenuPriceID + i).setText("")
                            }

                            sale!!.price = sumPrice

                            foodSaleView.findViewById<TextView>(R.id.food_sale_total_price).text =
                                DecimalFormat.getCurrencyInstance().format(sale!!.price).toString()
                            foodSaleView.findViewById<BootstrapEditText>(R.id.sell_main_food_count).setText("")
                        }
                    })

                    it.window!!.attributes = it.window!!.attributes.apply {
                        DisplayHelper.getDisplaySize().let { point ->
                            width = (point.x * 0.9).toInt()
//                            height = (point.y * 0.5).toInt()
                        }
                    }
                }

            executePendingBindings()
        }
    }

    private val spinnerItemClick: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            food = foods[position]

            val linearLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val labelWeight = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                weight = 1f
            }
            val editWeight = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                weight = 2f
            }

            foodSaleView.findViewById<LinearLayout>(R.id.sell_sub_food_layout).removeAllViews()

            for (i in 0 until foods[position].sub.size) {
                val layout = LinearLayout(context).apply {
                    layoutParams = linearLayoutParams
                    orientation = LinearLayout.HORIZONTAL
                }

                val label = BootstrapLabel(context).apply {
                    bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                    text = foods[position].sub[i].name
                }

                val edit = BootstrapEditText(context).apply {
                    setId(subMenuPriceID + i)
                    setTextColor(Color.BLACK)
                    inputType = InputType.TYPE_CLASS_NUMBER
                }

                layout.addView(label, labelWeight)
                layout.addView(edit, editWeight)
                foodSaleView.findViewById<LinearLayout>(R.id.sell_sub_food_layout).addView(layout)
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

    private val addSaleClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(item: Any?) {
            Toast.makeText(context, "addFood", Toast.LENGTH_SHORT).show()

            executePendingBindings()
        }
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
                    this@SellFragment.foods = foods
                }

                executePendingBindings()
            }
        })
    }

    private fun executePendingBindings() {
        binding.executePendingBindings()
    }
}