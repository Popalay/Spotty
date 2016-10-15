package com.popalay.spotty.ui.base.presenter

import android.support.annotation.CheckResult

import com.trello.rxlifecycle.LifecycleTransformer
import com.trello.rxlifecycle.OutsideLifecycleException
import com.trello.rxlifecycle.RxLifecycle

import rx.Observable
import rx.functions.Func1

object RxPresenterLifecycle {

    @CheckResult
    fun <T> bindPresenter(lifecycle: Observable<PresenterEvent>): LifecycleTransformer<T> {
        return RxLifecycle.bind<T, PresenterEvent>(lifecycle, PRESENTER_LIFECYCLE)
    }

    private val PRESENTER_LIFECYCLE = Func1<PresenterEvent, PresenterEvent> { lastEvent ->
        when (lastEvent) {
            PresenterEvent.ATTACH_VIEW -> PresenterEvent.DETACH_VIEW
            else -> throw OutsideLifecycleException("Cannot bind to Controller lifecycle when outside of it.")
        }
    }
}