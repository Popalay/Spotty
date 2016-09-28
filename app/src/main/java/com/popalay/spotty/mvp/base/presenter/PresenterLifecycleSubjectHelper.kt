package com.popalay.spotty.mvp.base.presenter

import com.hannesdorfmann.mosby.mvp.MvpView
import rx.subjects.BehaviorSubject

object PresenterLifecycleSubjectHelper {

    fun <V : MvpView> create(presenter: RxPresenter<V>): BehaviorSubject<PresenterEvent> {
        val subject = BehaviorSubject.create(PresenterEvent.ATTACH_VIEW)

        presenter.addLifecycleListener(object : RxPresenter.LifecycleListener<V>() {
            override fun detachView(retainInstance: Boolean) {
                subject.onNext(PresenterEvent.DETACH_VIEW)
            }

            override fun attachView(view: V) {
                subject.onNext(PresenterEvent.ATTACH_VIEW)
            }

        })
        return subject
    }

}