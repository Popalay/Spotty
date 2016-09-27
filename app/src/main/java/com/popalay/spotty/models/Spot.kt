package com.popalay.spotty.models

import com.google.android.gms.maps.model.LatLng


data class Spot(var id: String = "",
                var title: String = "",
                var description: String = "",
                var address: String = "",
                var position: LatLng = LatLng(0.0, 0.0),
                var authorEmail: String = ""
)