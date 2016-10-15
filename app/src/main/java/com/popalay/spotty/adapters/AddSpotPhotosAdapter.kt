package com.popalay.spotty.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import com.popalay.spotty.R
import com.popalay.spotty.utils.extensions.toPx
import kotlinx.android.synthetic.main.item_add_photo.view.*
import java.util.*

class AddSpotPhotosAdapter : UltimateAdapter<ViewHolder>(), UltimateAdapter.FooterInterface {

    val items: MutableList<Uri> = ArrayList()

    var onRemoveItem: ((Uri) -> Unit)? = null
    var selectedItem: Uri? = null

    init {
        setFooterVisibility(true)
    }

    override fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun add(uri: Uri) {
        if (!items.contains(uri)) {
            items.add(uri)
            notifyDataSetChanged()
        }
    }

    fun remove(uri: Uri) {
        items.remove(uri)
        selectedItem = null
    }


    override fun getDataSize() = items.size

    override fun getDataViewResId(viewType: Int) = R.layout.item_add_photo

    override fun getDataId(dataPosition: Int) = dataPosition.toLong()

    override fun getDataViewType(dataPosition: Int) = 1

    override fun getDataViewHolder(v: View, dataViewType: Int): ViewHolder {
        val vh = ViewHolder(v)
        vh.itemView.photo.setOnLongClickListener {
            val item = getItemByViewHolder(vh, items)
            selectedItem = item
            notifyDataSetChanged()
            true
        }
        vh.itemView.btn_remove.setOnClickListener {
            items.remove(selectedItem)
            onRemoveItem?.invoke(selectedItem!!)
            selectedItem = null
            notifyDataSetChanged()
        }
        return vh
    }

    override fun bindDataVH(vh: ViewHolder, dataPosition: Int) {
        val item = items[dataPosition]
        with(vh.itemView) {
            photo.setImageURI(item.toString())
            btn_remove.visibility = if (selectedItem == item) View.VISIBLE else View.GONE
            val margin = if (selectedItem == item) 16.toPx() else 0
            photo.setPadding(margin, margin, margin, margin)
        }
    }

    override fun getFooterVH(v: View) = FooterViewHolder(v)

    override fun getFooterViewResId() = R.layout.footer_add_photo

    override fun bindFooterVH(vh: RecyclerView.ViewHolder) {
    }
}