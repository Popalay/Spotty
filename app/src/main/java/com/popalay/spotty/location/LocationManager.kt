package com.popalay.spotty.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import ru.solodovnikov.rxlocationmanager.kotlin.LocationTime
import ru.solodovnikov.rxlocationmanager.kotlin.RxLocationManager
import rx.Observable


class LocationManager(context: Context) {

    val locationManager: RxLocationManager

    init {
        locationManager = RxLocationManager(context)
    }

    fun getLastLocation(): Observable<Location> = locationManager
            .getLastLocation(LocationManager.PASSIVE_PROVIDER, LocationTime.OneHour())

}