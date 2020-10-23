package ru.dilgorp.android.travelplanner.data

import java.util.*

data class Place(
    val uuid: UUID,
    val name: String,
    val description: String,
    val imagePath: String
)