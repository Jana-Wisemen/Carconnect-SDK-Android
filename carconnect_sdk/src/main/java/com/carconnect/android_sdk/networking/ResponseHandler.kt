package com.carconnect.android_sdk.networking

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

internal object ResponseHandler {
    suspend fun <T : Any> doCall(call: Call<T>): CarConnectResult<T> {
        return try {
            withContext(Dispatchers.IO) {
                val response = call.execute()
                if (response.isSuccessful) {
                    CarConnectResult.Success(response.body()!!)
                } else {
                    CarConnectResult.Failure(
                        response.errorBody().toString(),
                        null
                    )
                }
            }
        } catch (ex: Exception) {
            CarConnectResult.Failure(
                null,
                ex
            )
        }
    }
}