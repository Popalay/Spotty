package com.popalay.spotty.ui.dashboard

import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.Spot
import com.popalay.spotty.ui.base.presenter.PresenterEvent
import com.popalay.spotty.ui.base.presenter.RxPresenter
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DashboardPresenter : RxPresenter<DashboardView>() {

    @Inject lateinit var dataManager: DataManager

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: DashboardView) {
        super.attachView(view)
        loadData()
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
    }

    fun loadData() {
        dataManager.getSpots()
                .compose(bindUntilEvent<List<Spot>>(PresenterEvent.DETACH_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it.isEmpty()) view?.showError(RuntimeException("No spots yet"), false)
                }
                .filter { it.isNotEmpty() }
                .doOnNext {
                    view?.setData(it)
                    view?.showContent()
                }
                .subscribe()
    }

    fun addSpot() {
        view?.startAddSpot()
    }

    fun openSpot(spot: Spot) {
        view?.openSpot(spot)
    }
}