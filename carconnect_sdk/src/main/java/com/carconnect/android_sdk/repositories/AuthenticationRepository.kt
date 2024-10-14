package com.carconnect.android_sdk.repositories

import android.net.Uri
import com.carconnect.android_sdk.CarConnect
import com.carconnect.android_sdk.models.authentication.AuthenticationOptions

object AuthenticationRepository {

    fun getAuthenticationUrl(options: AuthenticationOptions, carConnect: CarConnect): Uri {
        return Uri.parse(carConnect.environment.baseUrl)
            .buildUpon()
            .appendEncodedPath("onboarding/authentication")
            .appendQueryParameter("clientId", carConnect.clientId)
            .apply {
                options.username?.let{
                    appendQueryParameter("username", it)
                }
                options.brand?.let {
                    appendQueryParameter("brand", it)
                }
            }
            .build()
    }

}