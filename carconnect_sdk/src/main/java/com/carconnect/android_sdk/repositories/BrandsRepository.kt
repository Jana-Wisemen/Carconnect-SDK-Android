package com.carconnect.android_sdk.repositories

import com.carconnect.android_sdk.CarConnect
import com.carconnect.android_sdk.models.Brand
import com.carconnect.android_sdk.networking.CarConnectResult
import com.carconnect.android_sdk.networking.ResponseHandler
import com.carconnect.android_sdk.networking.RestClient

internal object BrandsRepository {

    suspend fun getBrands(): CarConnectResult<List<Brand>> =
        ResponseHandler.doCall(RestClient.apiClient.getBrands(CarConnect.getInstance().clientId))

}