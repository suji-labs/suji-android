package com.suji.android.suji_android.helper

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodSaleListAdapter
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodCreateDialogBinding
import com.suji.android.suji_android.databinding.FoodSellDialogBinding

class DialogHelper private constructor(context: Context, private var layout: Int, builder: DialogHelper.Builder) :
    Dialog(context, R.style.AppTheme_AppCompat_CustomDialog) {
    private lateinit var binding: ViewDataBinding
    private lateinit var foods: ArrayList<Food>
    private var food: Food? = null
    private var subMenuPriceID: Int = 0x6000

    var positive: DialogInterface.OnClickListener? = null
    var negative: DialogInterface.OnClickListener? = null
    var neutral: DialogInterface.OnClickListener? = null

    init {
        this.positive = builder.positive
        this.negative = builder.negative
        this.neutral = builder.neutral
    }

    data class Builder(
        val context: Context,
        val layout: Int,
        var positive: DialogInterface.OnClickListener? = null,
        var negative: DialogInterface.OnClickListener? = null,
        var neutral: DialogInterface.OnClickListener? = null
    ) {
        fun setPositiveButton(title: String, listener: DialogInterface.OnClickListener) =
            apply { this.positive = listener }

        fun setNegativeButton(title: String, listener: DialogInterface.OnClickListener) =
            apply { this.negative = listener }

        fun setNeutralButton(title: String, listener: DialogInterface.OnClickListener) =
            apply { this.neutral = listener }

        fun build() = DialogHelper(context, layout, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window?.attributes = layoutParams
        val point = DisplayHelper.Singleton.getDisplaySize()
        window?.attributes?.width = (point.x * 0.9).toInt()
        window?.attributes?.height = (point.y * 0.7).toInt()
    }

    private fun initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, null, false)

        if (layout == R.layout.food_create_dialog) {
            (binding as FoodCreateDialogBinding).positive = positive
            (binding as FoodCreateDialogBinding).negative = negative
            (binding as FoodCreateDialogBinding).neutral = neutral
        } else if (layout == R.layout.food_sell_dialog) {
            (binding as FoodSellDialogBinding).sellItemSpinner.adapter = FoodSaleListAdapter(foods)
            (binding as FoodSellDialogBinding).sellItemSpinner.onItemSelectedListener = spinnerItemClick
//            (binding as FoodSellDialogBinding).listener = foodSellClickListener
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
                (binding as FoodSellDialogBinding).sellSubFoodLayout.addView(layout)
            }
        }
    }

//    private val foodSellClickListener: CreateSaleClickListener = object : CreateSaleClickListener {
//        override fun createSale() {
//            var sumPrice = 0
//            val formatter = DecimalFormat("###,###")
//
//            sumPrice += findViewById<BootstrapEditText>(R.id.sell_main_food_count).text.toString().toInt() * food!!.price
//            for (i in 0 until food!!.sub.size) {
//                sumPrice += findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString().toInt() * food!!.sub[i].price
//            }
//
//            (viewModel as SellViewModel).insert(Sale(food!!.name, formatter.format(sumPrice), DateTime()))
//            dismiss()
//        }
//
//        override fun cancel() {
//            dismiss()
//        }
//    }
}