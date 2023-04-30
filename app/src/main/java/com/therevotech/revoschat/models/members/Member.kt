package com.therevotech.revoschat.models.members

data class Member(
    val imageUrl: String,
    val location: Location?,
    val username: String
)