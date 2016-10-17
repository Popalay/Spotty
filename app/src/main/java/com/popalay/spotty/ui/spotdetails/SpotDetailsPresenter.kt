package com.popalay.spotty.ui.splash

import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.Spot
import com.popalay.spotty.ui.base.presenter.RxPresenter
import com.popalay.spotty.ui.spotdetails.SpotDetailsView
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SpotDetailsPresenter(val spot: Spot) : RxPresenter<SpotDetailsView>() {

    @Inject lateinit var dataManager: DataManager

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SpotDetailsView) {
        super.attachView(view)
        view.setBaseInfo(spot)
        getSpotAuthor()
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
    }

    private fun getSpotAuthor() {
        dataManager.getUser(spot.authorId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.setAuthor(it) }
    }
}
