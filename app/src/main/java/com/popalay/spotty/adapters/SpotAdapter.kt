package com.popalay.spotty.adapters

import android.view.View
import com.popalay.spotty.R
import com.popalay.spotty.models.Spot
import kotlinx.android.synthetic.main.item_spot.view.*
import java.util.*

class SpotAdapter : UltimateAdapter<ViewHolder>() {

    var items: MutableList<Spot> = ArrayList()

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
            name.text = item.title
            photo.setImageURI(item.photoUrls.firstOrNull())
        }
    }
}