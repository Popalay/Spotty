package com.popalay.spotty.adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import com.popalay.spotty.R
import java.util.*

class AddSpotPhotosAdapter : UltimateAdapter<ViewHolder>(), UltimateAdapter.HeaderInterface {

    var items: MutableList<Bitmap> = ArrayList()

    override fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getDataSize() = items.size

    override fun getDataViewResId(viewType: Int) = R.layout.item_spot

    override fun getDataId(dataPosition: Int) = dataPosition.toLong()

    override fun getDataViewType(dataPosition: Int) = 1

    override fun getDataViewHolder(v: View, dataViewType: Int) = ViewHolder(v)

    override fun bindDataVH(vh: ViewHolder, dataPosition: Int) {
        val item = items[dataPosition]
        with(vh.itemView) {

        }
    }

    override fun getHeaderVH(v: View) = HeaderViewHolder(v)

    override fun getHeaderViewResId() = R.layout.header_add_photo

    override fun bindHeaderVH(vh: RecyclerView.ViewHolder) {
    }
}