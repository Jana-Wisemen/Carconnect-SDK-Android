package com.carconnect.android_sdk.models.authentication

import java.io.Serializable

data class AuthenticationResult(
    val token: String,
    val vin: String
)
