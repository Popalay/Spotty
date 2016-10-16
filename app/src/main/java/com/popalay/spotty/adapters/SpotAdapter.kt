package com.popalay.spotty.adapters

import android.graphics.Bitmap
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.BasePostprocessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.popalay.spotty.R
import com.popalay.spotty.models.Spot
import com.popalay.spotty.utils.extensions.paletteColors
import com.popalay.spotty.utils.extensions.toUri
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
            address.text = item.placeName
            likesCount.text = item.counts.likes.toString()
            commentsCount.text = item.counts.comments.toString()

            val uri = item.photoUrls.first().toUri()

            val imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(object : BasePostprocessor() {
                        override fun process(bitmap: Bitmap?) {
                            bitmap.paletteColors { bgColor, textColor ->
                                this@with.spotContainer.setBackgroundColor(bgColor)
                                //this@with.name.setTextColor(textColor)
                            }
                        }
                    })
                    .build()
            val controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest).setOldController(photo.controller)
                    .build()
            photo.controller = controller
        }
    }

}