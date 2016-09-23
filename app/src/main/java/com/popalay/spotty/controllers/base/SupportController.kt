package com.popalay.spotty.controllers.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.bluelinelabs.conductor.rxlifecycle.RxController
import com.popalay.spotty.activities.BaseActivity

abstract class SupportController(args: Bundle?) : RxController(args) {

    protected constructor() : this(null) {
    }

    fun getBaseActivity(): BaseActivity {
        val activity = activity
        if (activity is BaseActivity) {
            return activity
        } else {
            throw RuntimeException("Activity mast implement BaseActivity")
        }

    }

    protected fun setSupportActionBar(toolbar: Toolbar) {
        getBaseActivity().setSupportActionBar(toolbar)
    }

    protected fun setTitle(title: String) {
        getBaseActivity().supportActionBar?.title = title
    }
}


