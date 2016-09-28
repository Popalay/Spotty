package com.popalay.spotty.mvp.base.presenter

import android.support.annotation.CheckResult

import rx.Observable

interface PresenterLifecycleProvider {

    @CheckResult
    fun lifecycle(): Observable<PresenterEvent>

    @CheckResult
    fun <T> bindUntilEvent(event: PresenterEvent): Observable.Transformer<T, T>

    @CheckResult
    fun <T> bindToLifecycle(): Observable.Transformer<T, T>
}