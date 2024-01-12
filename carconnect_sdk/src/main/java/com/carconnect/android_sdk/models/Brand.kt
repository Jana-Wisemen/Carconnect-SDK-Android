package com.carconnect.android_sdk.models

import android.net.Uri

data class Brand(
    val brand: String,
    val content: Content,
    val options: Options
){

    data class Content(
        val name: String,
        val description: String?,
        val images: Images
    )

    data class Images(
        val small: Uri?,
        val large: Uri?
    )

    data class Options(
        val beta: String,
        val regions: Regions,
        val fleetOnly: Boolean,
        val aaos: Boolean
    )

    data class Regions(
        val whileList: List<String>?,
        val blackList: List<String>?
    )

}
