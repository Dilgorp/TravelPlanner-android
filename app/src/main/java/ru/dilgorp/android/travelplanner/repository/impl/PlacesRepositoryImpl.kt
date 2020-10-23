package ru.dilgorp.android.travelplanner.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.CityPlace
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.network.CityPlacesApiService
import ru.dilgorp.android.travelplanner.network.SearchApiService
import ru.dilgorp.android.travelplanner.network.response.Response
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.PlacesRepository
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService,
    private val loginDataProvider: LoginDataProvider,
    private val placesApiService: CityPlacesApiService
) : PlacesRepository {

    private val _message = MutableLiveData<String>()
    override val message: LiveData<String>
        get() = _message

    private val _places = MutableLiveData<List<Place>>()
    override val places: LiveData<List<Place>>
        get() = _places

    override suspend fun refreshPlaces(uuid: UUID) {
        try {
            val response = apiService.getPlaces(loginDataProvider.credentials.value!!, uuid)
            if(response.type == ResponseType.ERROR){
                throw IllegalStateException(response.message)
            }else{
                _places.postValue(response.data)
            }
        }catch (e: Exception){
            _message.postValue(e.localizedMessage)
        }
    }

    override suspend fun addPlaces(city: City, selectedPlaces: List<Place>): Response<List<CityPlace>> {
        return try{
            placesApiService.addCityPlaces(
                loginDataProvider.credentials.value ?: "",
                city.userUuid,
                city.travelUuid,
                city.uuid,
                selectedPlaces
            )
        }catch (e: Exception){
            _message.postValue(e.message)
            Response(ResponseType.ERROR, "", null)
        }
    }

    override fun messageShown() {
        setMessage("")
    }

    override fun setMessage(messageString: String) {
        _message.value = ""
    }
}