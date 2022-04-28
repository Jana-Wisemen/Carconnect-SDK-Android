package com.carconnect.android_sdk.models

enum class Environment(val baseUrl: String) {
    production("https://carconnect.io/"),
    development("https://development.carconnect.io/")
}