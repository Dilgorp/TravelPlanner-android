package ru.dilgorp.android.travelplanner.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dilgorp.android.travelplanner.data.UserRequest
import ru.dilgorp.android.travelplanner.network.SearchApiService
import ru.dilgorp.android.travelplanner.network.response.Response
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.CityRepository
import java.lang.Exception
import java.lang.IllegalStateException
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val searchApiService: SearchApiService,
    private val loginDataProvider: LoginDataProvider
): CityRepository {

    private val _message = MutableLiveData<String>()
    override val message: LiveData<String>
        get() = _message

    override fun messageShown() {
        setMessage("")
    }

    override fun setMessage(messageString: String) {
        _message.value = messageString
    }

    override suspend fun getCityInfo(cityname: String): Response<UserRequest> {
        return try {
            val response = searchApiService.getCity(loginDataProvider.credentials.value!!, cityname)
            if(response.type == ResponseType.ERROR){
                throw IllegalStateException(response.message)
            }
            response
        }catch (e :Exception){
            _message.postValue(e.localizedMessage)
            Response(ResponseType.ERROR, e.localizedMessage ?: "", null)
        }
    }
}