package com.suji.android.suji_android.helper

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.suji.android.suji_android.R
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.food.FoodViewModel

class DialogHelper : Dialog {
    private lateinit var item: Food
    private lateinit var foodViewModel: FoodViewModel

    constructor(context: Context) : super(context)

    constructor(context: Context, food: Food) : super(context, R.style.AppTheme_AppCompat_CustomDialog) {
        this.item = food
    }

    constructor(context: Context, food: Food, foodViewModel: FoodViewModel) : super(context, R.style.AppTheme_AppCompat_CustomDialog) {
        this.item = food
        this.foodViewModel = foodViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_layout)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window?.attributes = layoutParams
        val point = DisplayHelper.Singleton.getDisplaySize()
        window?.attributes?.width = (point.x * 0.9).toInt()
        window?.attributes?.height = (point.y * 0.7).toInt()
    }
}