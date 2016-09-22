package com.popalay.spotty.auth

import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.kelvinapps.rxfirebase.RxFirebaseAuth
import com.pawegio.kandroid.d
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.models.User
import rx.schedulers.Schedulers
import java.util.*


class AuthManager(private val authProviderFactory: AuthProviderFactory) {

    private var authProvider: AuthProvider? = null

    interface AuthListener {
        fun authCompleted()
        fun authFailed()
    }

    val firebaseAuth: FirebaseAuth
    val authListeners: MutableList<AuthListener>

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        authListeners = ArrayList()
    }

    fun addAuthListener(authListener: AuthListener) {
        authListeners.add(authListener)
    }

    fun removeAuthListener(authListener: AuthListener) {
        authListeners.remove(authListener)
    }

    fun handleSignIn(requestCode: Int, resultCode: Int, data: Intent?) {
        authProvider?.handleSignIn(requestCode, resultCode, data, firebaseAuth) { task: Task<AuthResult>, user: User ->
            task.addOnCompleteListener {
                d("handle signin")
                if (it.isSuccessful) {
                    authCompleted(user)
                } else {
                    d("${it.exception?.message}")
                    authFailed()
                }
            }
        }
    }

    fun signIn(authProviderName: ProviderName, controller: BaseController) {
        authProvider = authProviderFactory.getAuthProvider(authProviderName)
        if (authProvider == null) {
            authFailed()
            return
        }
        authProvider?.signIn(controller)
    }

    fun signOut() {
        if (authProvider == null) {
            RxFirebaseAuth.fetchProvidersForEmail(firebaseAuth, firebaseAuth.currentUser?.email!!)
                    .subscribeOn(Schedulers.io())
                    .map {
                        it.providers?.let {
                            d(it[0])
                            authProviderFactory.getAuthProvider(ProviderName.getByName(it[0]))
                        }
                    }
                    .subscribe {
                        it?.signOut()
                    }
        }
        firebaseAuth.signOut()
        authProvider?.signOut()
    }

    private fun authCompleted(user: User) {
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(user.displayName)
                .setPhotoUri(user.profilePhoto)
                .build()
        firebaseAuth.currentUser?.updateProfile(profileUpdates)
        authListeners.forEach(AuthListener::authCompleted)
    }

    private fun authFailed() {
        authListeners.forEach(AuthListener::authFailed)
    }

}