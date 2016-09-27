package com.popalay.spotty.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.popalay.spotty.models.Spot
import rx.schedulers.Schedulers


class DataManager(val firebaseAuth: FirebaseAuth, val firebaseDb: FirebaseDatabase) {

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isLogged() = firebaseAuth.currentUser != null

    fun saveSpot(spot: Spot) {
        val reference = firebaseDb.reference.child("spot").push()
        spot.authorEmail = getCurrentUser()?.email.orEmpty()
        spot.id = reference.key
        reference.setValue(spot)
    }

    fun getSpots() = RxFirebaseDatabase.observeChildEvent(firebaseDb.reference.child("spot")).subscribeOn(Schedulers.io())
}