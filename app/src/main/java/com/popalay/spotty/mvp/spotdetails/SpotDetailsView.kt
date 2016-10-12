package com.popalay.spotty.mvp.spotdetails

import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.models.Spot
import com.popalay.spotty.models.User


interface SpotDetailsView : MvpView {

    fun setBaseInfo(spot: Spot)
    fun setAuthor(user: User)
}