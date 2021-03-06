package com.popalay.spotty.auth.providers

import android.content.Context
import android.content.Intent
import com.bluelinelabs.conductor.Controller
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.pawegio.kandroid.w
import com.popalay.spotty.models.User
import com.vk.sdk.*
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUser
import com.vk.sdk.api.model.VKList


class VkAuthProvider(context: Context) : AuthProvider {

    override fun handleSignIn(requestCode: Int, resultCode: Int, data: Intent?,
                              firebaseAuth: FirebaseAuth, result: (Task<AuthResult>, User) -> Unit) {
        VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                firebaseAuth.fetchProvidersForEmail(res.email).addOnCompleteListener {
                    if (it.result.providers == null) {
                        getUserInfo(res.email) {
                            result(firebaseAuth.createUserWithEmailAndPassword(res.email, res.userId), it)
                        }
                    } else {
                        getUserInfo(res.email) {
                            result(firebaseAuth.signInWithEmailAndPassword(res.email, res.userId), it)
                        }
                    }
                }
            }

            private fun getUserInfo(email: String, result: (User) -> Unit) {
                VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                        "id,first_name,last_name,photo_100")).executeWithListener(object : VKRequest.VKRequestListener() {
                    override fun onComplete(response: VKResponse?) {
                        super.onComplete(response)
                        val vkUser = (response?.parsedModel as VKList<*>)[0] as VKApiUser
                        val user = User(displayName = vkUser.toString(),
                                email = email,
                                profilePhoto = vkUser.photo_100)
                        result(user)
                    }
                })
            }

            override fun onError(error: VKError?) {
                w(error?.errorMessage.toString())
            }
        })
    }

    override fun signIn(controller: Controller) {
        val intent = Intent(controller.applicationContext, VKServiceActivity::class.java)
        intent.putExtra("arg1", VKServiceActivity.VKServiceType.Authorization.name)
        intent.putExtra("arg4", VKSdk.isCustomInitialize())
        intent.putStringArrayListExtra("arg2", arrayListOf(VKScope.EMAIL, VKScope.PHOTOS, VKScope.OFFLINE))
        controller.startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.outerCode)

    }

    override fun signOut() {
        VKSdk.logout()
    }
}

