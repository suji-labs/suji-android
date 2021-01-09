package com.suji.android.suji_android.account

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.suji.android.suji_android.base.BaseApp
import com.suji.android.suji_android.base.BaseViewModel
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Constant
import com.suji.android.suji_android.helper.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class AccountViewModel : BaseViewModel() {
    val soldList = MutableLiveData<MutableList<Sale>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        BaseApp.instance.getRepository().loadProduct(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    soldList.value = it
                },
                { it.printStackTrace() },
                { Log.d("AccountViewModel", "Data Load Done") }
            ).addTo(compositeDisposable)
    }

    fun getAllSold(): Flowable<MutableList<Sale>> {
        return BaseApp.instance.getRepository().loadProduct(true)
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

    fun findSoldDate(start: Long, end: Long): Flowable<MutableList<Sale>> {
        return BaseApp.instance.getRepository().findSoldDate(start, end)
    }

    fun deleteSoldDate(start: Long, end: Long) {
        BaseApp.instance.getRepository().deleteSoldDate(start, end)
    }

    fun computePrice(items: List<Sale>, type: Int): Int {
        var result = 0

        if (type == Constant.PayType.ALL) {
            items.forEach { sale ->
                result += sale.totalPrice
            }
        } else {
            items.forEach { sale ->
                if (sale.pay == type) {
                    result += sale.totalPrice
                }
            }
        }

        return result
    }
}