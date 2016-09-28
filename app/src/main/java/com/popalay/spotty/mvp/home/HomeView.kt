package com.popalay.spotty.mvp.home

import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.models.User


interface HomeView : MvpView {

    fun startSignIn()
    fun setUserInfo(user: User)
    fun openMap()
    fun openLikedSpots()
    fun openMySpots()
}