package com.popalay.spotty

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.popalay.spotty.di.*
import com.vk.sdk.VKSdk

class App : Application() {

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent

        @JvmStatic val sessionComponent: SessionComponent by lazy {
            appComponent.plus(SessionModule())
        }
    }

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        Fresco.initialize(this)
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

}
