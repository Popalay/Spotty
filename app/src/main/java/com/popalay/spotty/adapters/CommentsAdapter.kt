package com.popalay.spotty.adapters

import android.view.View
import com.popalay.spotty.R
import com.popalay.spotty.models.CommentUI
import kotlinx.android.synthetic.main.item_comment.view.*
import java.util.*

class CommentsAdapter : UltimateAdapter<ViewHolder>() {

    var items: MutableList<CommentUI> = ArrayList()

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
            commentAuthorProfileImage.setImageURI(item.authorPhotoUrl)
        }
    }
}
