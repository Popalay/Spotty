package com.popalay.spotty.ui.spotdetails

import com.popalay.spotty.App
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.Comment
import com.popalay.spotty.models.Spot
import com.popalay.spotty.models.UiComment
import com.popalay.spotty.ui.base.presenter.RxPresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class SpotDetailsPresenter(val spotId: String) : RxPresenter<SpotDetailsView>() {

    @Inject lateinit var dataManager: DataManager

    private lateinit var spot: Spot

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SpotDetailsView) {
        super.attachView(view)
        getSpot()
    }

    private fun getSpot() {
        dataManager.getSpot(spotId)
                .doOnNext {
                    spot = it
                    getSpotAuthor()
                    getSpotComments()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.setBaseInfo(it) }
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
    }

    private fun getSpotAuthor() {
        dataManager.getUser(spot.authorId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.setAuthor(it) }
    }

    private fun getSpotComments() {
        dataManager.getComments(spotId)
                .flatMap { Observable.from(it) }
                .flatMap({ dataManager.getUser(it.authorId) }) { comment, user ->
                    UiComment(user.profilePhoto.orEmpty(), comment.text)
                }
                .take(spot.counts.comments)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.setComments(it) }
    }

    fun saveComment(text: String) {
        if (!text.isNullOrBlank()) {
            spot.counts.comments++
            dataManager.getCurrentUser()
                    .doOnNext { dataManager.updateSpot(spot) }
                    .map { Comment(it.id.orEmpty(), text) }
                    .doOnNext { dataManager.saveComment(spot.id, it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        view?.showMessage("Comment has been saved")
                        view?.onCommentSaved()
                    }
        }
    }
}
