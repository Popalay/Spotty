package com.popalay.spotty.models

import android.net.Uri

data class User(var displayName: String? = null,
                var email: String? = null,
                var profilePhoto: Uri? = null)