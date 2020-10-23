package ru.dilgorp.android.travelplanner.repository

import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface TravelRepository: MessageRepository {
    suspend fun getCities(travelUuid: UUID) : Response<List<City>>
    suspend fun addCity(travelUuid: UUID): Response<City>
    suspend fun refreshTravel(travelUuid: UUID): Response<Travel>
    suspend fun deleteCity(city: City): Response<List<City>>
}