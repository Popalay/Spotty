package com.popalay.spotty.utils.ui.frescocontrollers

import android.graphics.drawable.Animatable
import com.facebook.common.logging.FLog
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo

class LoadListener(val onLoad: () -> Unit) : BaseControllerListener<ImageInfo>() {

    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        if (imageInfo == null) {
            return
        }
        val qualityInfo = imageInfo.qualityInfo
        FLog.d("Final image received! " + "Size %d x %d",
                "Quality level %d, good enough: %s, full quality: %s",
                imageInfo.width,
                imageInfo.height,
                qualityInfo.quality,
                qualityInfo.isOfGoodEnoughQuality,
                qualityInfo.isOfFullQuality)
        super.onFinalImageSet(id, imageInfo, animatable)
        onLoad()
    }

    override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
        FLog.d(javaClass, "Intermediate image received")
    }

    override fun onFailure(id: String?, throwable: Throwable?) {
        FLog.e(javaClass, throwable, "Error loading %s", id)
        onLoad()
    }
}

