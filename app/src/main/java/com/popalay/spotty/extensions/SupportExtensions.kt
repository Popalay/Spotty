package com.popalay.spotty.extensions

import android.app.Activity
import android.content.res.Resources
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Int.toDp():Int=(this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx():Int=(this * Resources.getSystem().displayMetrics.density).toInt()

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

fun Fragment.snackbar(text : CharSequence, duration: Int = Snackbar.LENGTH_LONG, init : Snackbar.() -> Unit = {}) : Snackbar {
    return view!!.snackbar(text, duration, init)
}

fun Fragment.snackbar(@StringRes text : Int, duration: Int = Snackbar.LENGTH_LONG, init : Snackbar.() -> Unit = {}) : Snackbar {
    return view!!.snackbar(text, duration, init)
}

fun Activity.snackbar(view: View, text : CharSequence, duration: Int = Snackbar.LENGTH_LONG, init : Snackbar.() -> Unit = {}) : Snackbar {
    return view.snackbar(text, duration, init)
}

fun Activity.snackbar(view: View, @StringRes text : Int, duration: Int = Snackbar.LENGTH_LONG, init : Snackbar.() -> Unit = {}) : Snackbar {
    return view.snackbar(text, duration, init)
}

fun Controller.snackbar(view: View, text : CharSequence, duration: Int = Snackbar.LENGTH_LONG, init : Snackbar.() -> Unit = {}) : Snackbar {
    return view.snackbar(text, duration, init)
}

fun Controller.snackbar(view: View, @StringRes text : Int, duration: Int = Snackbar.LENGTH_LONG, init : Snackbar.() -> Unit = {}) : Snackbar {
    return view.snackbar(text, duration, init)
}



