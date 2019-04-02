package com.suji.android.suji_android.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.adapter.FoodSaleListAdapter
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.FoodCreateDialogBinding
import com.suji.android.suji_android.databinding.FoodSellDialogBinding
import com.suji.android.suji_android.food.FoodViewModel
import com.suji.android.suji_android.listener.DialogClickListener

class DialogHelper : Dialog {
    private lateinit var binding: ViewDataBinding
    private lateinit var viewModel: ViewModel
    private lateinit var foods: ArrayList<Food>
    private var food: Food? = null
    private var layout: Int = 0
    private var subMenuLayoutID: Int = 0x8000
    private var subMenuNameID: Int = 0x7000
    private var subMenuPriceID: Int = 0x6000
    private var subMenuCount = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, layout: Int, viewModel: ViewModel)
            : super(context, R.style.AppTheme_AppCompat_CustomDialog) {
        this.viewModel = viewModel
        this.layout = layout
    }

    constructor(context: Context, layout: Int, food: Food, viewModel: ViewModel)
            : super(context, R.style.AppTheme_AppCompat_CustomDialog) {
        this.food = food
        this.viewModel = viewModel
        this.layout = layout
    }

    constructor(context: Context, layout: Int, viewModel: ViewModel, foods: ArrayList<Food>)
            : super(context, R.style.AppTheme_AppCompat_CustomDialog) {
        this.foods = foods
        this.viewModel = viewModel
        this.layout = layout
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
            (binding as FoodCreateDialogBinding).listener = createFoodDialog
        } else if (layout == R.layout.food_sell_dialog) {
            (binding as FoodSellDialogBinding).sellItemSpinner.adapter = FoodSaleListAdapter(foods)
            (binding as FoodSellDialogBinding).sellItemSpinner.onItemSelectedListener = spinnerItemClick
        }
    }

    private val spinnerItemClick: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val linearLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
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

                layout.addView(label, labelWeight)
                layout.addView(edit, editWeight)
                (binding as FoodSellDialogBinding).sellSubFoodLayout.addView(layout)
            }
        }
    }

    private val createFoodDialog: DialogClickListener = object : DialogClickListener {
        override fun createFood() {
            val subMenuList: ArrayList<Food> = ArrayList()
            val foodName: String = (binding as FoodCreateDialogBinding).createMenuEditName.text.toString()
            val foodPrice: String = (binding as FoodCreateDialogBinding).createMenuEditPrice.text.toString()

            if (foodName == "" || foodPrice == "") {
                Toast.makeText(context, "이름과 가격을 정확하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                return
            }

            if (subMenuCount > 0) {
                for (i in 0 until subMenuCount) {
                    val subMenuName = findViewById<BootstrapEditText>(subMenuNameID + i).text.toString()
                    val subMenuPrice = findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString()
                    subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                }
            }

            if (subMenuList.size == 0) {
                if (food == null) {
                    (viewModel as FoodViewModel).insert(Food(foodName, foodPrice.toInt()))
                } else {
                    (viewModel as FoodViewModel).update(Food(foodName, foodPrice.toInt(), id = food!!.id))
                }
            } else {
                if (food == null) {
                    (viewModel as FoodViewModel).insert(Food(foodName, foodPrice.toInt(), subMenuList))
                } else {
                    (viewModel as FoodViewModel).update(Food(foodName, foodPrice.toInt(), subMenuList, food!!.id))
                }
            }

            dismiss()
        }

        override fun addSubMenuClick() {
            val outerLayout: LinearLayout = findViewById(R.id.create_sub_menu)
            val innerLayout = LinearLayout(context)
            innerLayout.id = subMenuLayoutID + subMenuCount
            innerLayout.orientation = LinearLayout.HORIZONTAL
            innerLayout.gravity = Gravity.CENTER

            val nameLabel = BootstrapLabel(context)
            nameLabel.text = "부가 메뉴 이름"

            val nameEditText = BootstrapEditText(context)
            nameEditText.id = subMenuNameID + subMenuCount
            nameEditText.setTextColor(Color.BLACK)
            nameEditText.width = 150
            nameEditText.bottom = 15

            val priceLabel = BootstrapLabel(context)
            priceLabel.text = "부가 메뉴 가격"

            val priceEditText = BootstrapEditText(context)
            priceEditText.id = subMenuPriceID + subMenuCount
            priceEditText.setTextColor(Color.BLACK)
            priceEditText.inputType = InputType.TYPE_CLASS_NUMBER
            priceEditText.width = 150
            priceEditText.bottom = 15

            val subMenuDelete = BootstrapButton(context)
            subMenuDelete.id = subMenuCount
            subMenuDelete.text = "X"
            subMenuDelete.bootstrapBrand = DefaultBootstrapBrand.DANGER
            subMenuDelete.setOnClickListener(View.OnClickListener {
                if (subMenuCount < 0) {
                    return@OnClickListener
                }
                val layout = findViewById<LinearLayout>(subMenuLayoutID + it.id)
                outerLayout.removeView(layout)
                subMenuCount--
            })

            innerLayout.addView(nameLabel)
            innerLayout.addView(nameEditText)
            innerLayout.addView(priceLabel)
            innerLayout.addView(priceEditText)
            innerLayout.addView(subMenuDelete)

            outerLayout.addView(innerLayout)
            subMenuCount++
        }

        override fun cancelFood() {
            dismiss()
        }
    }
}