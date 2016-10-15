package com.popalay.spotty.ui.home

import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.models.User


interface HomeView : MvpView {

    fun startSignIn()
    fun setUserInfo(user: User)
    fun openDashboard()
    fun openMap()
    fun openLikedSpots()
    fun openMySpots()
}