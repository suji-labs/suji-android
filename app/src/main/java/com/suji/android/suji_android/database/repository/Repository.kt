package com.suji.android.suji_android.database.repository

import com.suji.android.suji_android.base.BaseApp
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

    fun loadAllFood(): MutableList<Food> {
        return BaseApp.instance.getDatabase().foodDAO().loadAllFood2()
//        return Flowable.create<MutableList<Food>> ({ emitter ->
//            BaseApp.instance.getDatabase().foodDAO().loadAllFood()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { result -> emitter.onNext(result) },
//                    { e -> emitter.onError(e) }
//                ).addTo(disposeBag)
//        }, BackpressureStrategy.BUFFER)
    }

    fun loadAllFood2(): Flowable<MutableList<Food>> {
        return Flowable.create<MutableList<Food>>({ emitter ->
            BaseApp.instance.getDatabase().foodDAO().loadAllFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> emitter.onNext(result) },
                    { e -> emitter.onError(e) }
                ).addTo(disposeBag)
        }, BackpressureStrategy.BUFFER)
    }

    fun loadProduct(isSale: Boolean): Flowable<MutableList<Sale>> {
        return Flowable.create<MutableList<Sale>> ({ emitter: FlowableEmitter<MutableList<Sale>> ->
            BaseApp.instance.getDatabase().saleDAO().loadProduct(isSale)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { result -> emitter.onNext(result) },
                    { e -> emitter.onError(e) }
                ).addTo(disposeBag)
        }, BackpressureStrategy.BUFFER)
    }

    fun findSoldDate(start: Long, end: Long): Flowable<MutableList<Sale>> {
        return Flowable.create<MutableList<Sale>> ({ emitter: FlowableEmitter<MutableList<Sale>> ->
            BaseApp.instance.getDatabase().saleDAO().findSoldDate(start, end)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { result -> emitter.onNext(result) },
                    { e -> emitter.onError(e) }
                ).addTo(disposeBag)
        }, BackpressureStrategy.BUFFER)
    }

    fun deleteSoldDate(start: Long, end: Long) {
        Single
            .just(Pair(start, end))
            .subscribeOn(Schedulers.io())
            .subscribe(
                { BaseApp.instance.getDatabase().saleDAO().deleteSoldDate(it.first, it.second) },
                { e -> e.printStackTrace()}
            ).addTo(disposeBag)
    }
}