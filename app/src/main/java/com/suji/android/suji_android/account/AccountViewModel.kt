package com.suji.android.suji_android.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.helper.Constant
import io.reactivex.Flowable
import org.threeten.bp.LocalDateTime

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    fun getAllSold(): Flowable<List<Sale>> {
        return BasicApp.instance.getRepository().loadProduct(true)
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

    fun findSoldDate(start: Long, end: Long): Flowable<List<Sale>> {
        return BasicApp.instance.getRepository().findSoldDate(start, end)
    }

    fun deleteSoldDate(start: Long, end: Long) {
        BasicApp.instance.getRepository().deleteSoldDate(start, end)
    }

    fun computePrice(items: List<Sale>, type: Int): Int {
        var result = 0

        if (type == Constant.PayType.ALL) {
            items.forEach { sale ->
                result += sale.price
            }
        } else {
            items.forEach { sale ->
                if (sale.pay == type) {
                    result += sale.price
                }
            }
        }

        return result
    }
}