package com.popalay.spotty.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kelvinapps.rxfirebase.DataSnapshotMapper
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.kelvinapps.rxfirebase.RxFirebaseStorage
import com.popalay.spotty.models.Spot
import com.popalay.spotty.models.User
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*


class DataManager(val firebaseAuth: FirebaseAuth, val firebaseDb: FirebaseDatabase) {

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isLogged() = firebaseAuth.currentUser != null

    fun saveSpot(spot: Spot, photos: MutableList<Uri>): Observable<Boolean> {
        val reference = firebaseDb.reference.child("spot").push()
        spot.authorEmail = getCurrentUser()?.email.orEmpty()
        spot.id = reference.key

        val photosRef = FirebaseStorage.getInstance().reference.child("photos").child(spot.id)
        val photoUrls: MutableList<String> = ArrayList()
        return Observable.from(photos)
                .subscribeOn(Schedulers.io())
                .flatMap { RxFirebaseStorage.putFile(photosRef.child("photo-${photos.indexOf(it)}"), it) }
                .doOnNext { photoUrls.add(it.downloadUrl.toString()) }
                .skip(photos.size - 1)
                .doOnNext { spot.photoUrls = photoUrls }
                .doOnNext { reference.setValue(spot) }
                .map { true }

    }

    //todo wth SOF Exception
    fun saveUser(user: User) {
        val reference = firebaseDb.reference.child("usedr").push()
        reference.setValue(user)
    }

    fun getSpots(): Observable<MutableList<Spot>> {
        return RxFirebaseDatabase.observeValueEvent(firebaseDb.reference.child("spot"),
                DataSnapshotMapper.listOf(Spot::class.java))
                .subscribeOn(Schedulers.io())
    }
}