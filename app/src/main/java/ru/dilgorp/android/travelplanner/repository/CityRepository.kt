package ru.dilgorp.android.travelplanner.repository

import ru.dilgorp.android.travelplanner.data.UserRequest
import ru.dilgorp.android.travelplanner.network.response.Response

interface CityRepository : MessageRepository{
    suspend fun getCityInfo(cityname: String): Response<UserRequest>
}