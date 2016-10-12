package com.popalay.spotty.mvp.splash

import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.Spot
import com.popalay.spotty.mvp.base.presenter.RxPresenter
import com.popalay.spotty.mvp.spotdetails.SpotDetailsView
import javax.inject.Inject


class SpotDetailsPresenter(val spot: Spot) : RxPresenter<SpotDetailsView>() {

    @Inject lateinit var dataManager: DataManager

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SpotDetailsView) {
        super.attachView(view)
        view.setBaseInfo(spot)
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
    }
}