package com.popalay.spotty.ui.base

import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby.mvp.MvpPresenter
import com.hannesdorfmann.mosby.mvp.MvpView
import com.hannesdorfmann.mosby.mvp.conductor.MvpController
import com.popalay.spotty.utils.extensions.snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseController<V : MvpView, P : MvpPresenter<V>>() : MvpController<V, P>() {

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

    protected fun setTitle(@StringRes title: Int) {
        getBaseActivity().supportActionBar?.setTitle(title)
    }

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


