package com.suji.android.suji_android.sell

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import io.reactivex.Flowable

class SellViewModel(application: Application) : AndroidViewModel(application) {
    fun getAllSale(): Flowable<List<Sale>> {
        return BasicApp.instance.getRepository().loadProduct(false)
    }

    fun insert(sale: Sale) {
        BasicApp.instance.getRepository().insert(sale)
    }

    fun delete(sale: Sale) {
        BasicApp.instance.getRepository().delete(sale)
    }

    fun update(sale: Sale) {
        BasicApp.instance.getRepository().update(sale)
    }

    fun sumOfPrice(sale: Sale): Int {
        var subSumPrice = 0
        var mainSumPrice = 0

        sale.foods.iterator().let { main ->
            while (main.hasNext()) {
                val food = main.next()
                mainSumPrice += food.price * food.count

                for (sub in food.sub) {
                    subSumPrice += sub.price * sub.count
                }
            }
        }

        return mainSumPrice + subSumPrice
    }
}