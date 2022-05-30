package com.carconnect.android_sdk.repositories

import com.carconnect.android_sdk.CarConnect
import com.carconnect.android_sdk.models.ConnectionCount
import com.carconnect.android_sdk.networking.CarConnectResult
import com.carconnect.android_sdk.networking.ResponseHandler
import com.carconnect.android_sdk.networking.RestClient

object ConnectionRepository {

    suspend fun getTotalConnections(email: String): CarConnectResult<ConnectionCount> =
        ResponseHandler.doCall(RestClient.apiClient.getTotalConnections(CarConnect.getInstance().clientId, email = email))

}