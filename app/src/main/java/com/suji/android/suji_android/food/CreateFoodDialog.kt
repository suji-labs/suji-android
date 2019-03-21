package com.suji.android.suji_android.food

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.callback.CreateSubMenuClick
import com.suji.android.suji_android.databinding.CreateFoodBinding
import com.suji.android.suji_android.helper.DisplayHelper
import com.suji.android.suji_android.model.Food


class CreateFoodDialog : AppCompatActivity() {
    private lateinit var binding: CreateFoodBinding
    private var foodViewModel: FoodViewModel = FoodViewModel(BasicApp.app)
    private var subMenuLayoutID: Int = 0x8000
    private var subMenuNameID: Int = 0x7000
    private var subMenuPriceID: Int = 0x6000
    private var subMenuCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams
        initView()
        val point = DisplayHelper.Singleton.getDisplaySize()
        window.attributes.width = (point.x * 0.9).toInt()
        window.attributes.height = (point.y * 0.7).toInt()
    }

    private fun initViewModel() {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, com.suji.android.suji_android.R.layout.create_food)
        binding.addMenu = addMenu
    }

    private var addMenu: CreateSubMenuClick = object : CreateSubMenuClick {
        override fun createFood() {
            val foodName: String = binding.createMenuEditName.text.toString()
            val foodPrice: String = binding.createMenuEditPrice.text.toString()

            if (foodName == "" || foodPrice == "") {
                return
            }

            if (subMenuCount > 0) {
                val subMenuList: ArrayList<Food> = ArrayList()

                for (i in 0 until subMenuCount) {
                    val subMenuName = findViewById<BootstrapEditText>(subMenuNameID + i).text.toString()
                    val subMenuPrice = findViewById<BootstrapEditText>(subMenuPriceID + i).text.toString()
                    subMenuList.add(Food(subMenuName, subMenuPrice.toInt()))
                    foodViewModel.addFood(Food(foodName, foodPrice.toInt(), subMenuList))
                }
            } else {
                foodViewModel.addFood(Food(foodName, foodPrice.toInt()))
            }
            finish()
        }

        override fun addSubMenuClick() {
            val outerLayout: LinearLayout = findViewById(com.suji.android.suji_android.R.id.create_sub_menu)
            val innerLayout = LinearLayout(applicationContext)
            innerLayout.id = subMenuLayoutID + subMenuCount
            innerLayout.orientation = LinearLayout.HORIZONTAL
            innerLayout.gravity = Gravity.CENTER

            val nameLabel = BootstrapLabel(applicationContext)
            nameLabel.text = "부가 메뉴 이름"

            val nameEditText = BootstrapEditText(applicationContext)
            nameEditText.id = subMenuNameID + subMenuCount
            nameEditText.setTextColor(Color.BLACK)
            nameEditText.width = 150
            nameEditText.bottom = 15

            val priceLabel = BootstrapLabel(applicationContext)
            priceLabel.text = "부가 메뉴 가격"

            val priceEditText = BootstrapEditText(applicationContext)
            priceEditText.id = subMenuPriceID + subMenuCount
            priceEditText.setTextColor(Color.BLACK)
            priceEditText.inputType = InputType.TYPE_CLASS_NUMBER
            priceEditText.width = 150
            priceEditText.bottom = 15

            val subMenuDelete = BootstrapButton(applicationContext)
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

        override fun cancelMenu() {
            finish()
        }
    }
}