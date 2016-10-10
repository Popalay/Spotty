package com.popalay.spotty.models

import com.google.android.gms.maps.model.LatLng


data class Spot(var id: String = "",
                var title: String = "",
                var description: String = "",
                var address: String = "",
                var position: Position = Position(),
                var authorEmail: String = "",
                var photoUrls: List<String> = emptyList()
)

data class Position(val latitude: Double = 0.0, val longitude: Double = 0.0) {
    fun toLatLng() = LatLng(latitude, longitude)
}