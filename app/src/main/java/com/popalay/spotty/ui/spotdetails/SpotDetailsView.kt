package com.popalay.spotty.ui.spotdetails

import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.models.Comment
import com.popalay.spotty.models.Spot
import com.popalay.spotty.models.UiComment
import com.popalay.spotty.models.User


interface SpotDetailsView : MvpView {

    fun setBaseInfo(spot: Spot)
    fun setAuthor(user: User)
    fun onCommentSaved()
    fun showMessage(text: String)
    fun setComments(comments: List<UiComment>)
}