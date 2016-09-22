package com.popalay.spotty.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.pawegio.kandroid.d
import com.pawegio.kandroid.w
import com.popalay.spotty.controllers.base.SupportController
import com.vk.sdk.*
import com.vk.sdk.api.VKError


class VkAuthProvider(context: Context) : AuthProvider {

    override fun handleSignIn(requestCode: Int, resultCode: Int, data: Intent?,
                              firebaseAuth: FirebaseAuth, result: (Task<AuthResult>) -> Unit) {
        VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                d(res.accessToken!!)
                result(firebaseAuth.signInWithCustomToken(res.accessToken!!))
            }

            override fun onError(error: VKError?) {
                w(error?.errorMessage.toString())
            }
        })
    }

    override fun signIn(controller: SupportController) {
        val intent = Intent(controller.applicationContext, VKServiceActivity::class.java)
        intent.putExtra("arg1", VKServiceActivity.VKServiceType.Authorization.name)
        intent.putExtra("arg4", VKSdk.isCustomInitialize())
        intent.putStringArrayListExtra("arg2", arrayListOf(VKScope.PHOTOS, VKScope.OFFLINE))
        controller.startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.outerCode)

    }

    override fun signOut() {
        VKSdk.logout()
    }
}

