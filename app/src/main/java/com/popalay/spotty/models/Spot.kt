package com.popalay.spotty.models

import com.google.android.gms.maps.model.LatLng


data class Spot(var id: String = "",
                var title: String = "",
                var description: String = "",
                var placeName: String = "",
                var address: String = "",
                var position: Position = Position(),
                var authorId: String = "",
                var photoUrls: List<String> = emptyList(),
                var counts: Counts = Counts()
)

data class Position(val latitude: Double = 0.0, val longitude: Double = 0.0) {
    fun toLatLng() = LatLng(latitude, longitude)
}

data class Counts(var likes: Int = 0, var comments: Int = 0)