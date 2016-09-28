package com.popalay.spotty.mvp.addspot

import com.google.android.gms.location.places.Place
import com.hannesdorfmann.mosby.mvp.MvpView


interface AddSpotView : MvpView {
    fun showError(message: String)
    fun onSpotSaved()
    fun pickPlace()
    fun showPickedPlace(place: Place)
}