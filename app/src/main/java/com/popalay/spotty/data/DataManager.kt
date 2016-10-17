package com.popalay.spotty.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.kelvinapps.rxfirebase.DataSnapshotMapper
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.kelvinapps.rxfirebase.RxFirebaseStorage
import com.popalay.spotty.models.Comment
import com.popalay.spotty.models.Spot
import com.popalay.spotty.models.User
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*


class DataManager(val firebaseAuth: FirebaseAuth, val firebaseDb: FirebaseDatabase) {

    fun getCurrentUser(): Observable<User> = Observable.just(firebaseAuth.currentUser)
            .subscribeOn(Schedulers.io())
            .map { User(it?.uid, it?.displayName, it?.email, it?.photoUrl.toString()) }

    fun isLogged() = firebaseAuth.currentUser != null

    fun saveSpot(spot: Spot, photos: MutableList<Uri>): Observable<Boolean> {
        val reference = firebaseDb.reference.child("spot").push()
        spot.authorId = firebaseAuth.currentUser?.uid.orEmpty()
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

    fun updateSpot(spot: Spot) {
        val reference = firebaseDb.reference.child("spot").child(spot.id)
        reference.runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
            }

            override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                mutableData ?: return Transaction.success(mutableData)
                mutableData.value = spot
                return Transaction.success(mutableData)
            }

        })
    }

    fun saveUser(userId: String, user: User) {
        user.id = userId
        val reference = firebaseDb.reference.child("user").child(userId)
        reference.setValue(user)
    }

    fun getSpots(): Observable<MutableList<Spot>> {
        return RxFirebaseDatabase.observeValueEvent(firebaseDb.reference.child("spot"),
                DataSnapshotMapper.listOf(Spot::class.java))
                .subscribeOn(Schedulers.io())
    }

    fun getUser(id: String): Observable<User> {
        return RxFirebaseDatabase.observeValueEvent(firebaseDb.reference.child("user").child(id),
                DataSnapshotMapper.of(User::class.java))
                .subscribeOn(Schedulers.io())
    }

    fun getSpot(id: String): Observable<Spot> {
        return RxFirebaseDatabase.observeValueEvent(firebaseDb.reference.child("spot").child(id),
                DataSnapshotMapper.of(Spot::class.java))
                .subscribeOn(Schedulers.io())
    }

    fun saveComment(spotId: String, comment: Comment) {
        val reference = firebaseDb.reference.child("comment").child(spotId).push()
        reference.setPriority(-System.currentTimeMillis())
        reference.setValue(comment)
    }

    fun getComments(spotId: String): Observable<List<Comment>> {
        return RxFirebaseDatabase.observeValueEvent(
                firebaseDb.reference.child("comment").child(spotId).orderByPriority(),
                DataSnapshotMapper.listOf(Comment::class.java))
                .subscribeOn(Schedulers.io())
    }
}