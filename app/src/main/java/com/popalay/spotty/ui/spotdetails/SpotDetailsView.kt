package com.popalay.spotty.ui.spotdetails

import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.models.CommentUI
import com.popalay.spotty.models.FullSpotDetails


interface SpotDetailsView : MvpView {

    fun setBaseInfo(fullSpotDetails: FullSpotDetails)
    fun onCommentSaved()
    fun showMessage(text: String)
    fun setComments(comments: List<CommentUI>)
}