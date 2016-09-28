package com.popalay.spotty.mvp.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby.mvp.MvpPresenter
import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.extensions.snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseController<V : MvpView, P : MvpPresenter<V>>() : SupportController<V, P>() {

    protected abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflateView(inflater, container)
        onViewBound(view)
        return view
    }

    protected open fun onViewBound(view: View) {
    }

    protected fun hideProgressDialog() {
        getBaseActivity().hideProgressDialog()
    }

    protected fun showProgressDialog() {
        getBaseActivity().showProgressDialog()
    }

    protected fun showSnackbar(message: String) {
        snackbar(getBaseActivity().coordinator, message).show()
    }

    protected fun showSnackbar(message: Int) {
        snackbar(getBaseActivity().coordinator, message).show()
    }
}