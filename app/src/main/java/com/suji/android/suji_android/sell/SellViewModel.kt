package com.suji.android.suji_android.sell

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.suji.android.suji_android.base.BaseApp
import com.suji.android.suji_android.base.BaseViewModel
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class SellViewModel : BaseViewModel() {
    val sellList = MutableLiveData<MutableList<Sale>>()
    val showDialog = MutableLiveData<Unit>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        BaseApp.instance.getRepository().loadProduct(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    sellList.value = it
                },
                { it.printStackTrace() },
                { Log.d("SellViewModel", "Data Load Done") }
            ).addTo(compositeDisposable)
    }

    fun getAllSale(): Flowable<MutableList<Sale>> {
        return BaseApp.instance.getRepository().loadProduct(false)
    }

    fun insert(sale: Sale) {
        BaseApp.instance.getRepository().insert(sale)
    }

    fun delete(sale: Sale) {
        BaseApp.instance.getRepository().delete(sale)
    }

    fun update(sale: Sale) {
        BaseApp.instance.getRepository().update(sale)
    }

    fun sumOfPrice(sale: Sale): Int {
        var subSumPrice = 0
        var mainSumPrice = 0

        sale.orderedFoods.iterator().let { main ->
            while (main.hasNext()) {
                val food = main.next()
                mainSumPrice += food.price * food.count

                for (sub in food.subMenu) {
                    subSumPrice += sub.price * sub.count
                }
            }
        }

        return mainSumPrice + subSumPrice
    }
}