package com.dscvit.cadence.model

data class UserData(
    val display_name: String,
    val external_urls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val type: String,
    val uri: String
)