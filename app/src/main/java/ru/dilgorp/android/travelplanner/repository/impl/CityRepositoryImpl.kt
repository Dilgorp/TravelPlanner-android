package ru.dilgorp.android.travelplanner.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.CityPlace
import ru.dilgorp.android.travelplanner.network.CityPlacesApiService
import ru.dilgorp.android.travelplanner.network.SearchApiService
import ru.dilgorp.android.travelplanner.network.response.Response
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val searchApiService: SearchApiService,
    private val cityPlacesApiService: CityPlacesApiService,
    private val loginDataProvider: LoginDataProvider
) : CityRepository {

    private val _message = MutableLiveData<String>()
    override val message: LiveData<String>
        get() = _message

    override suspend fun getCityInfo(city: City, cityname: String): Response<City> {
        return try {
            val response = searchApiService.getCity(
                loginDataProvider.credentials.value ?: "",
                city.userUuid, city.travelUuid, city.uuid, cityname
            )
            if (response.type == ResponseType.ERROR) {
                throw IllegalStateException(response.message)
            }
            response
        } catch (e: Exception) {
            _message.postValue(e.localizedMessage)
            Response(ResponseType.ERROR, e.localizedMessage ?: "", null)
        }
    }

    override suspend fun getCityPlaces(city: City): Response<List<CityPlace>> {
        return try {
            val response = cityPlacesApiService.getCityPlaces(
                loginDataProvider.credentials.value ?: "",
                city.userUuid, city.travelUuid, city.uuid
            )
            if (response.type == ResponseType.ERROR) {
                throw IllegalStateException(response.message)
            }
            response
        } catch (e: Exception) {
            _message.postValue(e.localizedMessage)
            Response(ResponseType.ERROR, e.localizedMessage ?: "", null)
        }
    }

    override fun messageShown() {
        setMessage("")
    }

    override fun setMessage(messageString: String) {
        _message.value = messageString
    }
}