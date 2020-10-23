package ru.dilgorp.android.travelplanner.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.network.CitiesApiService
import ru.dilgorp.android.travelplanner.network.TravelsApiService
import ru.dilgorp.android.travelplanner.network.response.Response
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.TravelRepository
import java.util.*
import javax.inject.Inject

class TravelRepositoryImpl @Inject constructor(
    private val loginDataProvider: LoginDataProvider,
    private val travelsApiService: TravelsApiService,
    private val citiesApiService: CitiesApiService
) : TravelRepository {

    private val _message = MutableLiveData<String>()
    override val message: LiveData<String>
        get() = _message

    override suspend fun getCities(travelUuid: UUID): Response<List<City>> {
        return try{
            citiesApiService.getCities(
                loginDataProvider.credentials.value ?: "",
                loginDataProvider.userUuid!!,
                travelUuid
            )
        }catch (e: Exception){
            _message.postValue(e.message)
            Response(ResponseType.ERROR, "", null)
        }
    }

    override suspend fun addCity(travelUuid: UUID): Response<City> {
        return try{
            citiesApiService.addCity(
                loginDataProvider.credentials.value ?: "",
                loginDataProvider.userUuid!!,
                travelUuid
            )
        }catch (e: Exception){
            _message.postValue(e.message)
            Response(ResponseType.ERROR, "", null)
        }
    }

    override suspend fun refreshTravel(travelUuid: UUID): Response<Travel> {
        return try{
            travelsApiService.refreshTravel(
                    loginDataProvider.credentials.value ?: "",
                loginDataProvider.userUuid!!,
                travelUuid
            )
        }catch (e: Exception){
            _message.postValue(e.message)
            Response(ResponseType.ERROR, "", null)
        }
    }

    override suspend fun deleteCity(city: City): Response<List<City>> {
        return try{
            citiesApiService.deleteCity(
                loginDataProvider.credentials.value ?: "",
                city.userUuid,
                city.travelUuid,
                city.uuid
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
        _message.postValue(messageString)
    }
}