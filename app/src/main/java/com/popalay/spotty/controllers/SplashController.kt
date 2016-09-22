package com.popalay.spotty.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent
import com.google.firebase.auth.FirebaseAuth
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.BaseController
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashController : BaseController() {

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_splash, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        Observable.interval(3000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(bindUntilEvent<Long>(ControllerEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    router.replaceTopController(
                            if (FirebaseAuth.getInstance().currentUser == null) {
                                RouterTransaction.with(LoginController())
                            } else {
                                RouterTransaction.with(HomeController())
                                        .popChangeHandler(HorizontalChangeHandler())
                                        .pushChangeHandler(HorizontalChangeHandler())
                            }
                    )
                }
    }

}
