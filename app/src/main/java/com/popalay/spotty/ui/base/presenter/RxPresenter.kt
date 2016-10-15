package com.popalay.spotty.ui.base.presenter

import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby.mvp.MvpView
import com.trello.rxlifecycle.RxLifecycle
import rx.Observable
import rx.subjects.BehaviorSubject
import java.util.*


abstract class RxPresenter<V : MvpView> : MvpBasePresenter<V>(), PresenterLifecycleProvider {

    private val lifecycleSubject: BehaviorSubject<PresenterEvent>

    private var lifecycleListeners: MutableList<LifecycleListener<V>> = ArrayList()

    init {
        lifecycleSubject = PresenterLifecycleSubjectHelper.create(this)
    }

    fun addLifecycleListener(lifecycleListener: LifecycleListener<V>) {
        if (!lifecycleListeners.contains(lifecycleListener)) {
            lifecycleListeners.add(lifecycleListener)
        }
    }


    @CallSuper
    override fun attachView(view: V) {
        super.attachView(view)
        for (lifecycleListener in lifecycleListeners) {
            lifecycleListener.attachView(view)
        }
    }

    @CallSuper
    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        for (lifecycleListener in lifecycleListeners) {
            lifecycleListener.detachView(retainInstance)
        }
    }

    @CheckResult
    override fun lifecycle(): Observable<PresenterEvent> {
        return lifecycleSubject.asObservable()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: PresenterEvent): Observable.Transformer<T, T> {
        return RxLifecycle.bindUntilEvent<T, PresenterEvent>(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): Observable.Transformer<T, T> {
        return RxPresenterLifecycle.bindPresenter<T>(lifecycleSubject)
    }

    abstract class LifecycleListener<in V : MvpView> {
        abstract fun detachView(retainInstance: Boolean)
        abstract fun attachView(view: V)
    }

}
