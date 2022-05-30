package com.carconnect.android_sdk.networking

import com.carconnect.android_sdk.models.Brand
import com.carconnect.android_sdk.models.ConnectionCount
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CarConnectService {

    @GET("api/connect/v2/brands")
    fun getBrands(
        @Query("clientId", encoded = true) clientId: String
    ): Call<List<Brand>>

    @GET("api/connect/v2/connections/total")
    fun getTotalConnections(
        @Query("clientId", encoded = true) clientId: String,
        @Query("email", encoded = true) email: String
    ): Call<ConnectionCount>

}