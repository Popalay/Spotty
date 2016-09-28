package com.popalay.spotty.mvp.splash

import com.pawegio.kandroid.d
import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.mvp.base.presenter.RxPresenter
import com.popalay.spotty.mvp.base.presenter.PresenterEvent
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SplashPresenter : RxPresenter<SplashView>() {

    @Inject lateinit var dataManager: DataManager

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SplashView) {
        //super.attachView(view)
        Observable.interval(3000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(bindUntilEvent<Long>(PresenterEvent.DETACH_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (dataManager.isLogged()) {
                        view.userIsLogged()
                    } else {
                        view.userNotLogged()
                    }
                }
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        d("detach")
    }
}