package com.popalay.spotty.auth.providers

import android.content.Intent
import com.bluelinelabs.conductor.Controller
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.popalay.spotty.models.User


interface AuthProvider {

    fun handleSignIn(requestCode: Int, resultCode: Int, data: Intent?,
                     firebaseAuth: FirebaseAuth, result: (Task<AuthResult>, User) -> Unit)
    fun signIn(controller: Controller)
    fun signOut()
}