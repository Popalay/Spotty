package com.popalay.spotty.extensions

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation


fun ViewGroup.expand(targetHeight: Int) {
    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            this@expand.layoutParams.height = if (interpolatedTime == 1f)
                targetHeight
            else (targetHeight * interpolatedTime).toInt()
            this@expand.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    a.duration = (targetHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)
}

fun ViewGroup.colapse() {
    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    val initialHeight = this.measuredHeight
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1F) {
                this@colapse.visibility = View.GONE
            } else {
                this@colapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                this@colapse.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    a.duration = (initialHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)
}

