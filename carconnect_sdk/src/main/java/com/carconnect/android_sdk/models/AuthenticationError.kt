package com.carconnect.android_sdk.models

enum class AuthenticationError {
    /// There is already an authentication process ongoing
    alreadyAuthenticating,

    /// The user cancelled the authentication process
    cancelled,

    /// An unknown error occurred
    unknown,

    /// No tokens where received
    noTokens
}