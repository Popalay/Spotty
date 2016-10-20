package com.popalay.spotty.ui.spotdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.R
import com.popalay.spotty.adapters.PhotosPagerAdapter
import com.popalay.spotty.adapters.SpotDetailsAdapter
import com.popalay.spotty.models.CommentUI
import com.popalay.spotty.models.FullSpotDetails
import com.popalay.spotty.ui.base.BaseController
import com.popalay.spotty.utils.extensions.inflate
import com.popalay.spotty.utils.ui.StickyHeaderDecoration
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.controller_spot_details.view.*
import kotlinx.android.synthetic.main.header_comments.view.*


class SpotDetailsController() : SpotDetailsView, BaseController<SpotDetailsView, SpotDetailsPresenter>() {

    lateinit var spotId: String
    lateinit var detailsAdapter: SpotDetailsAdapter

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_spot_details, false)
    }

    constructor(spotId: String) : this() {
        this.spotId = spotId
    }

    override fun createPresenter() = SpotDetailsPresenter(spotId)

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        initUI(view)
    }

    private fun initUI(view: View) {
        setSupportActionBar(view.toolbar)
        getSupportActionBar()?.setHomeButtonEnabled(true)
        with(view) {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                router.popCurrentController()
            }
            /*          sendComment.setOnClickListener {
                          presenter.saveComment(commentText.text.trimInString())
                      }*/
        }
    }

    override fun setBaseInfo(fullSpotDetails: FullSpotDetails) {
        val photosPagerAdapter = PhotosPagerAdapter(fullSpotDetails.spot.photoUrls)
        detailsAdapter = SpotDetailsAdapter(fullSpotDetails)
        with(view) {
            collapsingToolbar.title = fullSpotDetails.spot.title
            photos_pager.adapter = photosPagerAdapter
            indicator.setViewPager(photos_pager)

            detailsList.adapter = detailsAdapter
            detailsList.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity)
                    .sizeResId(R.dimen.divider_size)
                    .colorResId(R.color.divider)
                    .marginResId(R.dimen.divider_margin, R.dimen.normal)
                    .build())
            detailsList.addItemDecoration(StickyHeaderDecoration(detailsAdapter))
        }
    }

    override fun onCommentSaved() {
        view.commentText.text = null
    }

    override fun showMessage(text: String) {
        showSnackbar(text)
    }

    override fun setComments(comments: List<CommentUI>) {
        detailsAdapter.items = comments.toMutableList()
        detailsAdapter.notifyDataSetChanged()
    }
}