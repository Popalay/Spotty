package com.popalay.spotty.adapters

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.R
import com.popalay.spotty.extensions.inflate
import kotlinx.android.synthetic.main.item_photo.view.*

internal class PhotosPagerAdapter(val photos: List<String>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = container.inflate(R.layout.item_photo)
        view.photo.setImageURI(photos[position])
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, item: Any) = view == item

    override fun getCount() = photos.size

}