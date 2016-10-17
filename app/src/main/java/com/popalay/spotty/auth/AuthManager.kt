package com.popalay.spotty.auth

import android.content.Intent
import com.bluelinelabs.conductor.Controller
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.kelvinapps.rxfirebase.RxFirebaseAuth
import com.pawegio.kandroid.d
import com.popalay.spotty.auth.providers.AuthProvider
import com.popalay.spotty.models.User
import com.popalay.spotty.utils.extensions.toUri
import rx.schedulers.Schedulers
import java.util.*


class AuthManager(private val authProviderFactory: AuthProviderFactory) {

    private var authProvider: AuthProvider? = null

    interface AuthListener {
        fun authCompleted(userId: String, user: User)
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

    fun signIn(authProviderName: ProviderName, controller: Controller) {
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
                            d("${it.firstOrNull()}")
                            authProviderFactory.getAuthProvider(ProviderName.getByName(it.firstOrNull()))
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
                .setPhotoUri(user.profilePhoto.toUri())
                .build()
        firebaseAuth.currentUser?.let { firebaseUser ->
            firebaseUser.updateProfile(profileUpdates)
            authListeners.forEach { it.authCompleted(firebaseUser.uid, user) }

        }
    }

    private fun authFailed() {
        authListeners.forEach(AuthListener::authFailed)
    }

}