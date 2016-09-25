package com.popalay.spotty.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class DataManager(val firebaseAuth: FirebaseAuth, val firebaseDb: FirebaseDatabase) {

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isLogged() = firebaseAuth.currentUser != null
}