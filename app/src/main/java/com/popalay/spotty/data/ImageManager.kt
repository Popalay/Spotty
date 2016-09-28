package com.popalay.spotty.data

import android.content.Context
import com.github.oliveiradev.lib.RxPhoto
import com.github.oliveiradev.lib.Transformers
import com.github.oliveiradev.lib.TypeRequest

class ImageManager(val context: Context) {

    fun pickPhoto() = RxPhoto.request(context, TypeRequest.GALLERY)
            .compose(Transformers.applySchedeulers())

    fun takePhoto() = RxPhoto.request(context, TypeRequest.CAMERA)
            .compose(Transformers.applySchedeulers())
}