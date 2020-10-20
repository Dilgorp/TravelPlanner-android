package ru.dilgorp.android.travelplanner.repository

import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface TravelRepository : MessageRepository {
    suspend fun getTravels(): Response<List<Travel>>
    suspend fun addTravel(): Response<Travel>
}