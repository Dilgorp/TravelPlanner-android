package ru.dilgorp.android.travelplanner.repository

import ru.dilgorp.android.travelplanner.network.response.CitySearchResponse

interface CityRepository : MessageRepository{
    suspend fun getCityInfo(cityname: String): CitySearchResponse
}