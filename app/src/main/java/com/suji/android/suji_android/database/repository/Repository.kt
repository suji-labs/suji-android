package com.suji.android.suji_android.database.repository

import android.util.Log
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.food_create_dialog.view.*
import org.joda.time.DateTime

object Repository {
    private val disposeBag = CompositeDisposable()

    fun insert(o: Any) {
        when (o) {
            is Food -> {
                Single
                    .just(o)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { AppDatabase.Singleton.getInstance().foodDAO().insert(it) },
                        { e -> e.printStackTrace() }
                    ).addTo(disposeBag)
            }
            is Sale -> {
                Single
                    .just(o)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { AppDatabase.Singleton.getInstance().saleDAO().insert(it) },
                        { e -> e.printStackTrace() }
                    ).addTo(disposeBag)
            }
        }
    }

    fun delete(o: Any) {
        when (o) {
            is Food -> {
                Single
                    .just(o)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { AppDatabase.Singleton.getInstance().foodDAO().delete(it) },
                        { e -> e.printStackTrace() }
                    ).addTo(disposeBag)
            }
            is Sale -> {
                Single
                    .just(o)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { AppDatabase.Singleton.getInstance().saleDAO().delete(it) },
                        { e -> e.printStackTrace() }
                    ).addTo(disposeBag)
            }
        }
    }

    fun update(o: Any) {
        when (o) {
            is Food -> {
                Single
                    .just(o)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { AppDatabase.Singleton.getInstance().foodDAO().update(it) },
                        { e -> e.printStackTrace() }
                    ).addTo(disposeBag)
            }
            is Sale -> {
                Single
                    .just(o)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { AppDatabase.Singleton.getInstance().saleDAO().update(it) },
                        { e -> e.printStackTrace() }
                    ).addTo(disposeBag)
            }
        }
    }

    fun loadAllFood(): Flowable<List<Food>> {
        return Flowable.create<List<Food>> ({ emitter: FlowableEmitter<List<Food>> ->
            BasicApp.instance.getDatabase().foodDAO().loadAllFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        Log.i("MainActivity", result.toString())
                        emitter.onNext(result)
                    },
                    { e -> emitter.onError(e) }
                ).addTo(disposeBag)
        }, BackpressureStrategy.BUFFER)
    }

    fun loadProduct(isSale: Boolean): Flowable<List<Sale>> {
        return Flowable.create<List<Sale>> ({ emitter: FlowableEmitter<List<Sale>> ->
            BasicApp.instance.getDatabase().saleDAO().loadProduct(isSale)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        Log.i("MainActivity", result.toString())
                        emitter.onNext(result)
                    },
                    { e -> emitter.onError(e) }
                ).addTo(disposeBag)
        }, BackpressureStrategy.BUFFER)
    }

    fun findSaleOfDate(start: DateTime, end: DateTime): Flowable<List<Sale>> {
        return Flowable.create<List<Sale>> ({ emitter: FlowableEmitter<List<Sale>> ->
            BasicApp.instance.getDatabase().saleDAO().findSaleOfDate(start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        Log.i("MainActivity", result.toString())
                        emitter.onNext(result)
                    },
                    { e -> emitter.onError(e) }
                ).addTo(disposeBag)
        }, BackpressureStrategy.BUFFER)
    }

    fun deleteSoldDate(start: DateTime, end: DateTime) {
        Single
            .just(Pair(start, end))
            .subscribeOn(Schedulers.io())
            .subscribe(
                { BasicApp.instance.getDatabase().saleDAO().deleteSoldDate(it.first, it.second) },
                { e -> e.printStackTrace()}
            ).addTo(disposeBag)

//        Single
//            .just(0)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                { BasicApp.instance.getDatabase().saleDAO().deleteSoldDate(start, end) },
//                { e -> e.printStackTrace()}
//            ).addTo(disposeBag)
    }
}