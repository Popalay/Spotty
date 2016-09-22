package com.popalay.spotty.controllers.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.extensions.snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseController(args: Bundle?) : SupportController(args) {

    protected constructor() : this(null) {
    }

    protected abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflateView(inflater, container)
        onViewBound(view)
        return view
    }

    protected open fun onViewBound(view: View) {
    }

    protected fun hideProgress() {
        getBaseActivity().hideProgress()
    }

    protected fun showProgress() {
        getBaseActivity().showProgress()
    }

    protected fun showSnackbar(message: String) {
        snackbar(getBaseActivity().coordinator, message).show()
    }

    protected fun showSnackbar(message: Int) {
        snackbar(getBaseActivity().coordinator, message).show()
    }

}