package com.suji.android.suji_android.food

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.suji.android.suji_android.base.BaseApp
import com.suji.android.suji_android.base.BaseViewModel
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.helper.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class FoodViewModel : BaseViewModel() {
    val foodList = MutableLiveData<MutableList<Food>>()
    val showDialog = MutableLiveData<Unit>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        BaseApp.instance.getRepository().loadAllFood2()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    foodList.value = it
                },
                { it.printStackTrace() },
                { Log.d("FoodViewModel", "Data Load Done") }
            ).addTo(compositeDisposable)
    }

    fun getAllFood() {
        BaseApp.instance.getRepository().loadAllFood2()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    foodList.value = it
                },
                { it.printStackTrace() },
                { Log.d("FoodViewModel", "Data Load Done") }
            ).addTo(compositeDisposable)
    }

    fun insert(food: Food) {
        BaseApp.instance.getRepository().insert(food)
    }

    fun delete(food: Food) {
        BaseApp.instance.getRepository().delete(food)
    }

    fun update(food: Food) {
        BaseApp.instance.getRepository().update(food)
    }

    fun showDialog() {
        showDialog.value = Unit
    }
}