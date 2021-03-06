package com.popalay.spotty.ui.spotsmap

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.location.LocationManager
import com.popalay.spotty.models.Spot
import com.popalay.spotty.ui.base.presenter.PresenterEvent
import com.popalay.spotty.ui.base.presenter.RxPresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.onErrorReturnNull
import javax.inject.Inject

class SpotsMapPresenter : RxPresenter<SpotsMapView>() {

    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var dataManager: DataManager

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SpotsMapView) {
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
                .filter { it.isNotEmpty() }
                .flatMap { Observable.from(it) }
                .doOnNext { view?.showMarker(it) }
                .subscribe()
    }

    fun getLastLocation() {
        locationManager.getLastLocation()
                .compose(bindUntilEvent<Location>(PresenterEvent.DETACH_VIEW))
                .onErrorReturnNull()
                .filter { it != null }
                .doOnNext { it.toString() }
                .map { LatLng(it!!.latitude, it.longitude) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.setLocation(it) }
    }
}