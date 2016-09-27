package com.popalay.spotty.mvp.base

import android.support.v7.widget.Toolbar
import com.hannesdorfmann.mosby.mvp.MvpPresenter
import com.hannesdorfmann.mosby.mvp.MvpView
import com.hannesdorfmann.mosby.mvp.conductor.MvpController
import com.popalay.spotty.activities.BaseActivity

abstract class SupportController<V : MvpView, P : MvpPresenter<V>>() : MvpController<V, P>() {

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

    protected fun getSupportActionBar() = getBaseActivity().supportActionBar

    protected fun setTitle(title: String) {
        getBaseActivity().supportActionBar?.title = title
    }
}


