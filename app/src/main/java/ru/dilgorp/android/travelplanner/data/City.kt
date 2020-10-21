package ru.dilgorp.android.travelplanner.data

import java.io.Serializable
import java.util.*

data class City(
    val uuid: UUID,
    val name: String,
    val description: String,
    val placesCount: Int,
    val travelUuid: UUID,
    val userRequestUuid: UUID?,
    val userUuid: UUID,

    val travelNumber: Int,
) : Serializable {
    companion object {
        const val ARG_NAME = "ru.dilgorp.android.travelplanner.data.City"
    }
}