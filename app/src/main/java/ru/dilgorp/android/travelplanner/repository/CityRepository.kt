package ru.dilgorp.android.travelplanner.repository

import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.CityPlace
import ru.dilgorp.android.travelplanner.network.response.Response

interface CityRepository : MessageRepository {
    suspend fun getCityInfo(city: City, cityname: String): Response<City>
    suspend fun getCityPlaces(city: City): Response<List<CityPlace>>
    suspend fun refreshCity(city: City): Response<City>
    suspend fun deleteCityPlace(cityPlace: CityPlace): Response<List<CityPlace>>
}