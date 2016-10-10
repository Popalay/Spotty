package com.popalay.spotty.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView


class ScrolledMapView : MapView {

    constructor(p0: Context?) : super(p0)
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1)
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)
    constructor(p0: Context?, p1: GoogleMapOptions?) : super(p0, p1)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_UP -> parent.requestDisallowInterceptTouchEvent(false)
        }
        super.dispatchTouchEvent(ev)
        return true
    }
}