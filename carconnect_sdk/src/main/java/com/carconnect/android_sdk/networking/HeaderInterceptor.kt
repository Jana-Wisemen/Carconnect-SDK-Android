package com.carconnect.android_sdk.networking

import android.content.res.Resources
import android.os.Build
import com.carconnect.android_sdk.CarConnect
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

internal class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        Locale.getDefault()
        val systemConfiguration = Resources.getSystem().configuration
        val locale = if (Build.VERSION.SDK_INT >= 24) systemConfiguration.locales.get(0) else Locale.getDefault()
        val languageCode = locale.language

        val requestBuilder = request.newBuilder()
        requestBuilder.addHeader("ACCEPT", "Accept-Language")
        requestBuilder.addHeader("Accept-Language", languageCode)
        requestBuilder.addHeader("SDK-Version", CarConnect.VERSION)
        val newRequest = requestBuilder.build()

        return chain.proceed(newRequest)
    }
}