package com.popalay.spotty.models


data class Spot(var id: String = "",
                var title: String = "",
                var description: String = "",
                var address: String = "",
                var position: Position = Position(),
                var authorEmail: String = ""
)

data class Position(val latitude: Double = 0.0, val longitude: Double = 0.0)