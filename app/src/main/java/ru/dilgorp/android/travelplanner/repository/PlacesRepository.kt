package ru.dilgorp.android.travelplanner.repository

import androidx.lifecycle.LiveData
import ru.dilgorp.android.travelplanner.data.Place
import java.util.*

interface PlacesRepository: MessageRepository {
    val places: LiveData<List<Place>>
    suspend fun refreshPlaces(uuid: UUID)
}