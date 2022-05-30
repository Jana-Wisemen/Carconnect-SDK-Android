package com.carconnect.android_sdk.helpers

import android.util.Log
import android.webkit.JavascriptInterface
import com.carconnect.android_sdk.models.authentication.Authentication

class JsMessageHandlerInterface(private val handler: Handler) {

    interface Handler {
        abstract fun onAuthentication(authentication: Authentication)
        abstract fun onStatus(status: String)
        abstract fun onTokens(json: String)
    }

    @JavascriptInterface
    fun authentication(json: String) {
        Log.d("JavascriptInterface", "authentication: $json")
        handler.onAuthentication(Authentication.fromJson(json))
    }

    @JavascriptInterface
    fun status(status: String) {
        Log.d("JavascriptInterface", "status: $status")
        handler.onStatus(status)
    }

    @JavascriptInterface
    fun tokens(json: String) {
        Log.d("JavascriptInterface", "tokens: $json")
        handler.onTokens(json)
    }
}