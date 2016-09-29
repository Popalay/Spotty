package com.popalay.spotty.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import com.pawegio.kandroid.d
import com.popalay.spotty.R
import kotlinx.android.synthetic.main.item_photo.view.*
import java.util.*

class AddSpotPhotosAdapter : UltimateAdapter<ViewHolder>(), UltimateAdapter.FooterInterface {

    val items: MutableList<Uri> = ArrayList()

    val selectedItems: MutableList<Int> = ArrayList()

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

    //todo
    override fun bindDataVH(vh: ViewHolder, dataPosition: Int) {
        val item = items[dataPosition]
        with(vh.itemView) {
            photo.setImageURI(item.toString())
            if (selectedItems.contains(dataPosition)) {
                d("selected")
                btn_remove.visibility = View.VISIBLE
                val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT)
                val margin = resources.getDimension(R.dimen.small).toInt()
                params.setMargins(margin, margin, margin, margin)
                photo.layoutParams = params
            }
        }
    }

    override fun getFooterVH(v: View) = FooterViewHolder(v)

    override fun getFooterViewResId() = R.layout.footer_add_photo

    override fun bindFooterVH(vh: RecyclerView.ViewHolder) {
    }
}