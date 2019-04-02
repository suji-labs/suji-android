package com.suji.android.suji_android.food

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.repository.DataRepository

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository
    private var foods: MediatorLiveData<List<Food>> = MediatorLiveData()

    init {
        foods.value = null
        repository = (application as BasicApp).getRepository()
        val food = repository.food
        foods.addSource(food, object : Observer<List<Food>> {
            override fun onChanged(foods: List<Food>?) {
                this@FoodViewModel.foods.value = foods
            }
        })
    }

    fun getAllFood(): LiveData<List<Food>> {
        return foods
    }

    fun insert(food: Food) {
        repository.insert(food)
    }

    fun delete(food: Food) {
        repository.delete(food)
    }

    fun update(food: Food) {
        repository.update(food)
    }
}