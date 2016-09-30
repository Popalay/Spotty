package com.popalay.spotty.mvp.addspot

import android.net.Uri
import com.google.android.gms.location.places.Place
import com.hannesdorfmann.mosby.mvp.MvpView


interface AddSpotView : MvpView {
    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun onSpotSaved()
    fun pickPlace()
    fun showPickedPlace(place: Place)
    fun updatePhotosCount(count: Int)
    fun choosePhoto()
    fun removePhoto(photo: Uri)
    fun addPhoto(photo: Uri)
}