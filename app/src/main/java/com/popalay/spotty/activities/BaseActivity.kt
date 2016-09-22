package com.popalay.spotty.activities

import android.support.v7.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {

    abstract fun showProgress()

    abstract fun hideProgress()
}