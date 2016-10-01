package com.popalay.spotty.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kelvinapps.rxfirebase.DataSnapshotMapper
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.kelvinapps.rxfirebase.RxFirebaseStorage
import com.popalay.spotty.models.Spot
import rx.Observable
import rx.schedulers.Schedulers


class DataManager(val firebaseAuth: FirebaseAuth, val firebaseDb: FirebaseDatabase) {

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isLogged() = firebaseAuth.currentUser != null

    fun saveSpot(spot: Spot, photos: MutableList<Uri>): Observable<Boolean> {
        val reference = firebaseDb.reference.child("spot").push()
        spot.authorEmail = getCurrentUser()?.email.orEmpty()
        spot.id = reference.key
        reference.setValue(spot)

        val photosRef = FirebaseStorage.getInstance().reference.child("photos").child(spot.id)
        return Observable.from(photos)
                .subscribeOn(Schedulers.io())
                .flatMap { RxFirebaseStorage.putFile(photosRef.child("photo-${photos.indexOf(it)}"), it) }
                .skip(photos.size - 1)
                .map { true }

    }

    fun getSpots(): Observable<MutableList<Spot>> {
        return RxFirebaseDatabase.observeValueEvent(firebaseDb.reference.child("spot"),
                DataSnapshotMapper.listOf(Spot::class.java))
                .subscribeOn(Schedulers.io())
    }
}