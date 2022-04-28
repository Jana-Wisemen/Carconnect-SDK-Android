package com.carconnect.android_sdk.networking

sealed class CarConnectResult<out T> {
    data class Success<out R>(val value: R): CarConnectResult<R>()
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ): CarConnectResult<Nothing>()
}