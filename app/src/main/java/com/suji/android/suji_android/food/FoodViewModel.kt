package com.suji.android.suji_android.food

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Food
import io.reactivex.Flowable

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    fun getAllFood(): Flowable<List<Food>> {
        return BasicApp.instance.getRepository().loadAllFood()
    }

    fun insert(food: Food) {
        BasicApp.instance.getRepository().insert(food)
    }

    fun delete(food: Food) {
        BasicApp.instance.getRepository().delete(food)
    }

    fun update(food: Food) {
        BasicApp.instance.getRepository().update(food)
    }
}