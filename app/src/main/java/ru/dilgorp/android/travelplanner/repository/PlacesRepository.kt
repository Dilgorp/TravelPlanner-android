package ru.dilgorp.android.travelplanner.repository

import androidx.lifecycle.LiveData
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.CityPlace
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface PlacesRepository: MessageRepository {
    val places: LiveData<List<Place>>
    suspend fun refreshPlaces(uuid: UUID)
    suspend fun addPlaces(city: City, selectedPlaces: List<Place>): Response<List<CityPlace>>
}