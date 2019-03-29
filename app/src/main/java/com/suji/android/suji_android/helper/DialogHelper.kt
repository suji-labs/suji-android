package com.suji.android.suji_android.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.R
import com.suji.android.suji_android.callback.DialogClickListener
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.databinding.DialogLayoutBinding
import com.suji.android.suji_android.food.FoodViewModel

class DialogHelper : Dialog {
    private lateinit var binding: DialogLayoutBinding
    private lateinit var foodViewModel: FoodViewModel
    private var food: Food? = null
    private var subMenuLayoutID: Int = 0x8000
    private var subMenuNameID: Int = 0x7000
    private var subMenuPriceID: Int = 0x6000
    private var subMenuCount = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, foodViewModel: FoodViewModel)
            : super(context, R.style.AppTheme_AppCompat_CustomDialog) {
        this.foodViewModel = foodViewModel
    }

    constructor(context: Context, food: Food, foodViewModel: FoodViewModel)
            : super(context, R.style.AppTheme_AppCompat_CustomDialog) {
        this.food = food
        this.foodViewModel = foodViewModel
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_layout, null, false)
        binding.listener = listener
    }

    private var listener: DialogClickListener = object : DialogClickListener {
        override fun createFood() {
            val subMenuList: ArrayList<Food> = ArrayList()
            val foodName: String = binding.createMenuEditName.text.toString()
            val foodPrice: String = binding.createMenuEditPrice.text.toString()

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
                    foodViewModel.insert(Food(foodName, foodPrice.toInt()))
                } else {
                    foodViewModel.update(Food(foodName, foodPrice.toInt(), id = food!!.id))
                }
            } else {
                if (food == null) {
                    foodViewModel.insert(Food(foodName, foodPrice.toInt(), subMenuList))
                } else {
                    foodViewModel.update(Food(foodName, foodPrice.toInt(), subMenuList, food!!.id))
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