package com.carconnect.android_sdk.models.authentication

import android.net.Uri
import com.carconnect.android_sdk.networking.UriDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

data class Authentication private constructor(
    val url: String,
    val redirect: AuthenticationRedirect?
) {
    companion object {
        const val RESULT_TOKENS = "RESULT_TOKENS"

        fun fromJson(json: String): Authentication {
            val gson = GsonBuilder().registerTypeAdapter(Uri::class.java, UriDeserializer()).create()
            return gson.fromJson(json, object : TypeToken<Authentication>() {}.type)
        }
    }
}

data class AuthenticationRedirect(
    val url: Uri,
    val capture: String,
    val parameters: List<String>
)

fun Authentication.buildRedirect(url: Uri): Uri? {
    redirect ?: return null

    val redirectUrl = redirect.url
    val fragmentUri = Uri.parse("http://localhost?" + url.fragment)

    return when {
        url.queryParameterNames.containsAll(redirect.parameters) -> {
            redirectUrl.buildUpon().appendQueryParameter("params", url.query.toString()).build()
        }
        url.fragment != null && fragmentUri.queryParameterNames.containsAll(redirect.parameters) -> {
            redirectUrl.buildUpon().appendQueryParameter("params", fragmentUri.query).build()
        }
        else -> {
            redirectUrl
        }
    }
}