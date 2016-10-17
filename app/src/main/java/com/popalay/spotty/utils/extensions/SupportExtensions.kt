package com.popalay.spotty.utils.extensions


import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.graphics.Palette
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bluelinelabs.conductor.Controller
import com.popalay.spotty.R
import com.popalay.spotty.utils.ui.CircleTransform
import com.rohitarya.picasso.facedetection.transformation.FaceCenterCrop
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun View.snackbar(@StringRes text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun Fragment.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view!!.snackbar(text, duration, init)
}

fun Fragment.snackbar(@StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view!!.snackbar(text, duration, init)
}

fun Activity.snackbar(view: View, text: CharSequence, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view.snackbar(text, duration, init)
}

fun Activity.snackbar(view: View, @StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view.snackbar(text, duration, init)
}

fun Controller.snackbar(view: View, text: CharSequence, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view.snackbar(text, duration, init)
}

fun Controller.snackbar(view: View, @StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar {
    return view.snackbar(text, duration, init)
}


fun ImageView.loadInCircle(imageUrl: String?) {
    if (imageUrl.isNullOrBlank()) {
        Picasso.with(context).load(R.mipmap.ic_launcher)
                .transform(CircleTransform())
                .into(this)
    } else {
        Picasso.with(context).load(imageUrl)
                .fit()
                .centerInside()
                .transform(FaceCenterCrop(100, 100))
                .transform(CircleTransform())
                .into(this)
    }
}

fun ImageView.load(imageUrl: String?) {
    if (imageUrl.isNullOrBlank()) {
        Picasso.with(context).load(R.color.gray).into(this)
    } else {
        Picasso.with(context).load(imageUrl)
                .into(this)
    }
}

fun ImageView.loadToTarget(imageUrl: String?, callback: ((Bitmap) -> Unit)? = null) {
    if (imageUrl.isNullOrBlank()) {
        Picasso.with(context).load(R.color.gray).into(this)
    } else {
        Picasso.with(context).load(imageUrl)
                .into(object : Target {

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        this@loadToTarget.setImageDrawable(placeHolderDrawable)
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {
                        this@loadToTarget.setImageDrawable(errorDrawable)
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        this@loadToTarget.setImageBitmap(bitmap)
                        callback?.invoke(bitmap)
                    }

                })
    }
}

fun String?.toUri(): Uri = Uri.parse(this)

fun Uri?.orEmpty(): Uri = this ?: Uri.EMPTY

fun Bitmap?.paletteColors(result: (bgColor: Int, textColor: Int) -> Unit) {
    if (this == null) return
    Palette.from(this).generate {
        val swatch = it.dominantSwatch
        val bgColor = swatch?.rgb
        val textColor = swatch?.titleTextColor
        result(bgColor ?: Color.BLUE, textColor ?: Color.WHITE)
    }
}

