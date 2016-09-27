package com.popalay.spotty.mvp.splash

import com.hannesdorfmann.mosby.mvp.MvpPresenter
import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SplashPresenter : MvpPresenter<SplashView> {

    @Inject lateinit var dataManager: DataManager

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SplashView?) {
        Observable.interval(3000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .first()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (dataManager.isLogged()) {
                        view?.userIsLogged()
                    } else {
                        view?.userNotLogged()
                    }
                }
    }

    override fun detachView(retainInstance: Boolean) {
    }
}