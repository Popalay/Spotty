package com.popalay.spotty.data

import android.content.Context
import android.net.Uri
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import rx.Observable

class ImageManager(val context: Context) {

    fun pickPhoto(): Observable<Uri> = RxImagePicker.with(context).requestImage(Sources.GALLERY)
            //.subscribeOn(Schedulers.io())

    fun takePhoto(): Observable<Uri> = RxImagePicker.with(context).requestImage(Sources.CAMERA)
            //.subscribeOn(Schedulers.io())
}