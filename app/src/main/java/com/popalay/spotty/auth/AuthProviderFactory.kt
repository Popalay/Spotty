package com.popalay.spotty.auth

import android.content.Context
import android.text.TextUtils
import com.popalay.spotty.auth.providers.AuthProvider
import com.popalay.spotty.auth.providers.GoogleAuthProvider
import com.popalay.spotty.auth.providers.VkAuthProvider

enum class ProviderName(var providerName: String) {
    GOOGLE("google.com"), VK("password");

    companion object {
        fun getByName(name: String): ProviderName? {
            return values().find { TextUtils.equals(it.providerName, name) }
        }
    }
}

class AuthProviderFactory(var context: Context) {

    private var authProvider: AuthProvider? = null

    fun getAuthProvider(providerName: ProviderName?): AuthProvider? {
        if (providerName == null) return null
        return when (providerName) {
            ProviderName.GOOGLE -> {
                if (authProvider !is GoogleAuthProvider) {
                    authProvider = GoogleAuthProvider(context)
                }
                authProvider
            }
            ProviderName.VK -> {
                if (authProvider !is VkAuthProvider) {
                    authProvider = VkAuthProvider(context)
                }
                authProvider
            }
            else -> {
                authProvider = null
                authProvider
            }
        }
    }

}