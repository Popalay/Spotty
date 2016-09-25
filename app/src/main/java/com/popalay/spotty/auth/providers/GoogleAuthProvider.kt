package com.popalay.spotty.auth.providers

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.SupportController
import com.popalay.spotty.models.User

class GoogleAuthProvider(val context: Context) : AuthProvider, GoogleApiClient.OnConnectionFailedListener {

    private val GOOGLE_RC_SIGN_IN = 0

    private lateinit var googleApiClient: GoogleApiClient
    private var gso: GoogleSignInOptions

    init {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
    }

    override fun handleSignIn(requestCode: Int, resultCode: Int, data: Intent?,
                              firebaseAuth: FirebaseAuth, result: (Task<AuthResult>, User) -> Unit) {
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            val signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val account = signInResult.signInAccount
            account?.let {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                result(firebaseAuth.signInWithCredential(credential),
                        User(account.displayName, account.photoUrl))
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }


    override fun signIn(controller: SupportController) {
        googleApiClient = GoogleApiClient.Builder(context).enableAutoManage(controller.getBaseActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        controller.startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN)
        googleApiClient.stopAutoManage(controller.getBaseActivity())
    }

    override fun signOut() {
        if (googleApiClient.isConnected) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { }
        }
    }
}