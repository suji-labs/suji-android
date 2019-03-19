package com.suji.android.suji_android.food

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapEditText
import com.beardedhen.androidbootstrap.BootstrapLabel
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.suji.android.suji_android.callback.CreateSubMenuClick
import com.suji.android.suji_android.databinding.CreateFoodBinding
import com.suji.android.suji_android.helper.DisplayHelper


class CreateFoodDialog : Activity() {
    private lateinit var binding: CreateFoodBinding
    private var subMenuID: Int = 0x8000
    private var subMenuCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            Toast.makeText(applicationContext, "$foodName $foodPrice", Toast.LENGTH_SHORT).show()
        }

        override fun addSubMenuClick() {
            val outerLayout: LinearLayout = findViewById(com.suji.android.suji_android.R.id.create_sub_menu)
            val innerLayout = LinearLayout(applicationContext)
            innerLayout.id = subMenuID + subMenuCount
            innerLayout.orientation = LinearLayout.HORIZONTAL
            innerLayout.gravity = Gravity.CENTER

            val nameLabel = BootstrapLabel(applicationContext)
            nameLabel.text = "부가 메뉴 이름"

            val nameEditText = BootstrapEditText(applicationContext)
//            nameEditText.id = subMenuID
            nameEditText.width = 150
            nameEditText.bottom = 15

            val priceLabel = BootstrapLabel(applicationContext)
            priceLabel.text = "부가 메뉴 가격"

            val priceEditText = BootstrapEditText(applicationContext)
//            priceEditText.id = subMenuID
            priceEditText.width = 150
            priceEditText.bottom = 15

            val subMenuDelete = BootstrapButton(applicationContext)
            subMenuDelete.text = "X"
            subMenuDelete.bootstrapBrand = DefaultBootstrapBrand.DANGER
            subMenuDelete.setOnClickListener(View.OnClickListener {
                if (subMenuCount < 0) {
                    return@OnClickListener
                }
                val layout = findViewById<LinearLayout>(subMenuID + subMenuCount)
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