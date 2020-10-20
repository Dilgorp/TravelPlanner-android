package ru.dilgorp.android.travelplanner.data

import java.util.*

data class User(
    val uuid: UUID,
    val username: String,
    val password: String
)