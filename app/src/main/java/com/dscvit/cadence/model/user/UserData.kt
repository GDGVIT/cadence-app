package com.dscvit.cadence.model.user

data class UserData(
    val display_name: String,
    val email: String,
    val external_urls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val type: String,
    val uri: String
)