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
import com.beardedhen.androidbootstrap.BootstrapText
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodSaleListAdapter
import com.suji.android.suji_android.adapter.SellListAdapter
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.databinding.FoodSellDialogBinding
import com.suji.android.suji_android.databinding.SellFragmentBinding
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.listener.FloatingButtonClickListener
import com.suji.android.suji_android.listener.FoodSellClickListener
import kotlinx.android.synthetic.main.food_sell_dialog.*
import org.joda.time.DateTime
import java.text.DecimalFormat

class SellFragment : Fragment() {
    private lateinit var binding: SellFragmentBinding
    private lateinit var adapter: SellListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var sellViewModel: SellViewModel = SellViewModel(BasicApp.app)
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)
    private lateinit var foods: List<Food>
    private val formatter: DecimalFormat = DecimalFormat("###,###")
    private lateinit var inflater: LayoutInflater
    private lateinit var foodSaleView: View
    private var food: Food? = null
    private var sale: Sale? = null
    private val subMenuPriceID = 0x6000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<SellFragmentBinding>(inflater, R.layout.sell_fragment, container, false)
        initViewModel()
        binding.listener = floatingButtonClickListener
        adapter = SellListAdapter(foodSellClickListener)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.sellFragmentItems.layoutManager = layoutManager
        binding.sellFragmentItems.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflater = activity!!.layoutInflater
        foodSaleView = inflater.inflate(R.layout.food_sell_dialog, null)
    }

    private val floatingButtonClickListener: FloatingButtonClickListener = object : FloatingButtonClickListener {
        override fun sell() {
            foodSaleView.findViewById<Spinner>(R.id.sell_item_spinner).adapter = FoodSaleListAdapter(foods)
            foodSaleView.findViewById<Spinner>(R.id.sell_item_spinner).onItemSelectedListener = spinnerItemClick

            val dialog = AlertDialog.Builder(activity, R.style.AppTheme_AppCompat_CustomDialog)
                .setPositiveButton("판매", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (sale == null) {
                            Toast.makeText(context, "수량이 입력되지 않았습니다!", Toast.LENGTH_SHORT).show()
                        } else {
                            sellViewModel.insert(sale!!)

                            dialog!!.dismiss()
                        }

                        (foodSaleView.parent as ViewGroup).removeView(foodSaleView)
                    }
                })
                .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        (foodSaleView.parent as ViewGroup).removeView(foodSaleView)
                        dialog!!.dismiss()
                    }
                })
                .setNeutralButton("추가", null)
                .setView(foodSaleView)
                .show()

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    var sumPrice = 0

                    sumPrice += foodSaleView.findViewById<BootstrapEditText>(R.id.sell_main_food_count).text.toString().toInt() * food!!.price
                    for (i in 0 until food!!.sub.size) {
                        sumPrice += foodSaleView.findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString().toInt() * food!!.sub[i].price
                    }

                    if (sale == null) {
                        sale = Sale("총 금액", formatter.format(sumPrice), DateTime())
                    } else {
                        sale!!.price = formatter.format(formatter.parse(sale!!.price).toInt() + sumPrice)
                    }

                    foodSaleView.findViewById<TextView>(R.id.food_sale_total_price).text = sale!!.price
                    foodSaleView.findViewById<BootstrapEditText>(R.id.sell_main_food_count).setText("")
                }
            })
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
            val labelWeight = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            labelWeight.weight = 1f
            val editWeight = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            editWeight.weight = 2f

            foodSaleView.findViewById<LinearLayout>(R.id.sell_sub_food_layout).removeAllViews()

            for (i in 0 until foods[position].sub.size) {
                val layout = LinearLayout(context)
                layout.layoutParams = linearLayoutParams
                layout.orientation = LinearLayout.HORIZONTAL

                val label = BootstrapLabel(context)
                label.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                label.text = foods[position].sub[i].name

                val edit = BootstrapEditText(context)
                edit.id = subMenuPriceID + i
                edit.setTextColor(Color.BLACK)
                edit.inputType = InputType.TYPE_CLASS_NUMBER

                layout.addView(label, labelWeight)
                layout.addView(edit, editWeight)
                foodSaleView.findViewById<LinearLayout>(R.id.sell_sub_food_layout).addView(layout)
            }
        }
    }

    private val foodSellClickListener: FoodSellClickListener = object : FoodSellClickListener {
        override fun sell(sale: Sale) {
            sellViewModel.delete(sale)
        }

        override fun addFood() {
            Toast.makeText(context, "addFood", Toast.LENGTH_SHORT).show()
        }

        override fun cancel(sale: Sale) {
            sellViewModel.delete(sale)
        }
    }

    private fun initViewModel() {
        sellViewModel = ViewModelProviders.of(this).get(SellViewModel::class.java)
        sellViewModel.getAllSale().observe(this, object : Observer<List<Sale>> {
            override fun onChanged(@Nullable sales: List<Sale>?) {
                if (sales != null) {
                    adapter.setSaleList(sales)
                }

                binding.executePendingBindings()
            }
        })

        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        foodViewModel.getAllFood().observe(this, object : Observer<List<Food>> {
            override fun onChanged(@Nullable foods: List<Food>?) {
                if (foods != null) {
                    this@SellFragment.foods = foods
                }
            }
        })
    }
}