package ru.dilgorp.android.travelplanner.data

import java.util.*

data class CityPlace(
    val uuid: UUID,
    val name: String,
    val description: String,
    val cityUuid: UUID,
    val travelUuid: UUID,
    val userUuid: UUID
)