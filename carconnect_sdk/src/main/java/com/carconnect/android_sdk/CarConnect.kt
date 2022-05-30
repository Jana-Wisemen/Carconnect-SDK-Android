package com.carconnect.android_sdk

import android.content.Context
import android.content.Intent
import com.carconnect.android_sdk.models.authentication.AuthenticationOptions
import com.carconnect.android_sdk.models.Brand
import com.carconnect.android_sdk.models.ConnectionCount
import com.carconnect.android_sdk.models.Environment
import com.carconnect.android_sdk.networking.CarConnectResult
import com.carconnect.android_sdk.networking.RestClient
import com.carconnect.android_sdk.repositories.BrandsRepository
import com.carconnect.android_sdk.repositories.ConnectionRepository
import com.carconnect.android_sdk.ui.AuthenticationActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class CarConnect private constructor(
    val clientId: String,
    val environment: Environment = Environment.production
){

    companion object {
        const val VERSION = "1.0.0"
        private var instance: CarConnect? = null

        fun register(clientId: String, environment: Environment){
            instance = CarConnect(clientId, environment)
        }

        fun unregister(){
            RestClient.session.cancel("CarConnect session closed")
            instance = null
        }

        fun getInstance() = instance ?: throw IllegalStateException("CarConnect was not registered")
    }

    fun brands(handler: (CarConnectResult<List<Brand>>) -> Unit) {
        RestClient.session.launch {
            BrandsRepository.getBrands().run(handler)
        }
    }

    fun totalConnections(email: String, handler: (CarConnectResult<ConnectionCount>) -> Unit){
        RestClient.session.launch {
            ConnectionRepository.getTotalConnections(email).run(handler)
        }
    }

    fun authenticationIntent(context: Context, options: AuthenticationOptions): Intent{
        return Intent(context, AuthenticationActivity::class.java).apply {
            putExtra(AuthenticationActivity.EXTRA_USERNAME, options.username)
            putExtra(AuthenticationActivity.EXTRA_BRAND, options.brand)
        }
    }
}

