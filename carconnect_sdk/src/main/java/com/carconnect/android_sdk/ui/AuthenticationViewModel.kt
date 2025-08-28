package com.carconnect.android_sdk.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.carconnect.android_sdk.CarConnect
import com.carconnect.android_sdk.models.authentication.Authentication
import com.carconnect.android_sdk.models.authentication.AuthenticationOptions
import com.carconnect.android_sdk.repositories.AuthenticationRepository
import com.carconnect.android_sdk.util.doubleArgsViewModelFactory

class AuthenticationViewModel(val code: String?, brand: String?): ViewModel() {

    companion object{
        val factory = doubleArgsViewModelFactory(::AuthenticationViewModel)
    }

    private val options by lazy {
        AuthenticationOptions(code, brand)
    }

    fun getAuthenticationUrl(): Uri{
        return AuthenticationRepository.getAuthenticationUrl(options, CarConnect.getInstance()).also {
            Log.d("getAuthenticationUrl", it.toString())
        }
    }

    var authentication: Authentication? = null
}