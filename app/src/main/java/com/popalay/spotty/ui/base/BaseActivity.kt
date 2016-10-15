package com.popalay.spotty.ui.base

import android.support.v7.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {

    abstract fun showProgressDialog()

    abstract fun hideProgressDialog()
}