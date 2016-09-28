package com.popalay.spotty.mvp.addspot

import com.google.android.gms.location.places.Place
import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.Position
import com.popalay.spotty.models.Spot
import com.popalay.spotty.mvp.base.presenter.RxPresenter
import javax.inject.Inject


class AddSpotPresenter : RxPresenter<AddSpotView>() {

    @Inject lateinit var dataManager: DataManager

    private var pickedPlace: Place? = null

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: AddSpotView) {
        super.attachView(view)
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
    }

    fun saveSpot(spot: Spot) {
        if (pickedPlace == null) {
            view?.showError("Please, pick your place")
            return
        }
        pickedPlace?.let {
            spot.address = "${it.name}, ${it.address}"
            spot.position = Position(it.latLng.latitude, it.latLng.longitude)
        }
        dataManager.saveSpot(spot)
        view?.onSpotSaved()
    }

    fun pickPlace() {
        view?.pickPlace()
    }

    fun setPickedPlace(place: Place) {
        pickedPlace = place
        view?.showPickedPlace(place)
    }
}