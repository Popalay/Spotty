package com.popalay.spotty.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import com.popalay.spotty.R
import com.popalay.spotty.models.UiComment
import com.popalay.spotty.utils.ui.StickyHeaderAdapter
import kotlinx.android.synthetic.main.item_comment.view.*
import java.util.*

class CommentsAdapter : UltimateAdapter<ViewHolder>(), UltimateAdapter.HeaderInterface, StickyHeaderAdapter {
    var items: MutableList<UiComment> = ArrayList()

    override fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getDataSize() = items.size

    override fun getDataViewResId(viewType: Int) = R.layout.item_comment

    override fun getDataId(dataPosition: Int) = dataPosition.toLong()

    override fun getDataViewType(dataPosition: Int) = 1

    override fun getDataViewHolder(v: View, dataViewType: Int) = ViewHolder(v)

    override fun bindDataVH(vh: ViewHolder, dataPosition: Int) {
        val item = items[dataPosition]
        with(vh.itemView) {
            comment.text = item.text
            authorProfileImage.setImageURI(item.authorPhotoUrl)
        }
    }

    override fun getHeaderVH(v: View): HeaderVH {
        return HeaderViewHolder(v)
    }

    override fun getHeaderViewResId() = R.layout.header_comments

    override fun bindHeaderVH(vh: RecyclerView.ViewHolder) {
    }

    override fun getHeaderId(position: Int): Long {
        return getDataSize() / 3L
    }
}
