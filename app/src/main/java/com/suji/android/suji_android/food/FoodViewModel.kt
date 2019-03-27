package com.suji.android.suji_android.food

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.repository.DataRepository

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository
    private var items: MediatorLiveData<List<Food>> = MediatorLiveData()

    init {
        items.value = null
        repository = (application as BasicApp).getRepository()
        val menu = repository.menu
        items.addSource(menu, object : Observer<List<Food>> {
            override fun onChanged(foods: List<Food>?) {
                items.value = foods
            }
        })
    }

    fun getAllFood(): LiveData<List<Food>> {
        return items
    }

    fun addFood(food: Food) {
        repository.addFood(food)
    }

    fun deleteFood(food: Food) {
        repository.deleteFood(food)
    }

    fun modifyFood(food: Food) {
        repository.modifyFood(food)
    }
}