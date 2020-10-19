package ru.dilgorp.android.travelplanner.data

import java.util.*

data class Place(
    val uuid: UUID,
    val name: String,
    val description: String
) {
    companion object {
        const val CITY_UUID_NAME = "cityUUID"
    }
}