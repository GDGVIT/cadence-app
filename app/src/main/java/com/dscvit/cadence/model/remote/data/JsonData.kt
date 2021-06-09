package com.dscvit.cadence.model.remote.data

data class JsonData(
    val href: String,
    val items: List<Item>,
    val next: String,
    val offset: Int,
    val previous: Any,
    val total: Int
)