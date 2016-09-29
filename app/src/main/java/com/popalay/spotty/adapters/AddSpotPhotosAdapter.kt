package com.popalay.spotty.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import com.pawegio.kandroid.d
import com.popalay.spotty.R
import kotlinx.android.synthetic.main.item_photo.view.*
import java.util.*

class AddSpotPhotosAdapter : UltimateAdapter<ViewHolder>(), UltimateAdapter.FooterInterface {

    var items: MutableList<Uri> = ArrayList()

    init {
        setFooterVisibility(true)
    }

    override fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getDataSize() = items.size

    override fun getDataViewResId(viewType: Int) = R.layout.item_photo

    override fun getDataId(dataPosition: Int) = dataPosition.toLong()

    override fun getDataViewType(dataPosition: Int) = 1

    override fun getDataViewHolder(v: View, dataViewType: Int) = ViewHolder(v)

    override fun bindDataVH(vh: ViewHolder, dataPosition: Int) {
        val item = items[dataPosition]
        with(vh.itemView) {
            photo.setImageURI(item.toString())
        }
        d("bind $item")
    }

    override fun getFooterVH(v: View) = FooterViewHolder(v)

    override fun getFooterViewResId() = R.layout.footer_add_photo

    override fun bindFooterVH(vh: RecyclerView.ViewHolder) {
    }
}