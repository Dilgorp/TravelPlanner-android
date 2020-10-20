package ru.dilgorp.android.travelplanner.data

import java.io.Serializable
import java.util.*

data class Travel(
    val uuid: UUID,
    val name: String,
    val citiesCount: Int,
    val placesCount: Int,
    val userUuid: UUID
) : Serializable {
    companion object {
        const val ARG_NAME = "ru.dilgorp.android.travelplanner.data.Travel"
    }
}