package com.carconnect.android_sdk.networking

import android.net.Uri
import com.carconnect.android_sdk.BuildConfig
import com.carconnect.android_sdk.CarConnect
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

internal object RestClient {

    var session = CoroutineScope(Dispatchers.IO + Job())

    val apiClient by lazy { createRetrofit().create(CarConnectService::class.java) }

    private val baseUrl: String
        get() = CarConnect.getInstance().environment.baseUrl

    private val httpClient: OkHttpClient by lazy { createHttpClient() }
    private fun createHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BASIC
                    } else {
                        HttpLoggingInterceptor.Level.BODY
                    }
            })
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapter(Uri::class.java, UriDeserializer()).create()
                )
            )
            .build()
    }

}