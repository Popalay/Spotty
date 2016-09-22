package com.popalay.spotty.auth

import android.content.Context

enum class ProviderName(var providerName: String) {
    GOOGLE("google.com"), VK("vk.com");

    companion object {
        fun getByName(name: String): ProviderName? {
            return values().find { it.providerName === name }
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