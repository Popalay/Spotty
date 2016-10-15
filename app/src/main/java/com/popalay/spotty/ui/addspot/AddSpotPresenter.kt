package com.popalay.spotty.ui.addspot

import android.net.Uri
import com.google.android.gms.location.places.Place
import com.pawegio.kandroid.d
import com.popalay.spotty.App
import com.popalay.spotty.MAX_PHOTOS_COUNT
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.Position
import com.popalay.spotty.models.Spot
import com.popalay.spotty.ui.base.presenter.RxPresenter
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import javax.inject.Inject


class AddSpotPresenter : RxPresenter<AddSpotView>() {

    @Inject lateinit var dataManager: DataManager

    private val spot: Spot = Spot()
    private val photos: MutableList<Uri> = ArrayList()

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: AddSpotView) {
        super.attachView(view)
        view.updatePhotosCount(0)
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
    }

    fun saveSpot() {
        if (!validateSpot(spot)) return
        view?.showProgress()
        dataManager.saveSpot(spot, photos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.hideProgress()
                    view?.onSpotSaved()
                }

    }

    fun pickPlace() {
        view?.pickPlace()
    }

    fun setTitle(title: String) {
        spot.title = title
    }

    fun setDescription(description: String) {
        spot.description = description
    }

    fun setPickedPlace(place: Place) {
        spot.apply {
            address = "${place.name}, ${place.address}"
            position = Position(place.latLng.latitude, place.latLng.longitude)
        }
        view?.showPickedPlace(place)
    }

    private fun validateSpot(spot: Spot): Boolean {
        d(spot.toString())
        val error: String = with(spot) {
            when {
                address.isNullOrBlank() -> "Please, pick your place"
                title.isNullOrBlank() -> "Please, fill title"
                description.isNullOrBlank() -> "Please, fill description"
                photos.isEmpty() -> "Please, add photos"
                else -> ""
            }
        }
        return if (error.isNotBlank()) {
            view?.showError(error)
            false
        } else {
            true
        }
    }

    fun requestAddPhoto() {
        if (photos.size < MAX_PHOTOS_COUNT) {
            view?.choosePhoto()
        } else {
            view?.showError("You have max count of photos")
        }
    }

    fun addPhoto(photo: Uri) {
        if (!photos.contains(photo)) {
            photos.add(photo)
        } else {
            view?.showError("This photo is picked")
        }
        view?.addPhoto(photo)
        view?.updatePhotosCount(photos.size)
    }

    fun removePhoto(photo: Uri) {
        photos.remove(photo)
        view?.removePhoto(photo)
        view?.updatePhotosCount(photos.size)
    }
}