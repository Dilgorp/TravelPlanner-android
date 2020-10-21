package ru.dilgorp.android.travelplanner.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.network.TravelsApiService
import ru.dilgorp.android.travelplanner.network.response.Response
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.TravelsRepository
import javax.inject.Inject

class TravelsRepositoryImpl @Inject constructor(
    private val loginDataProvider: LoginDataProvider,
    private val travelsApiService: TravelsApiService
) : TravelsRepository {

    private val _message = MutableLiveData<String>()
    override val message: LiveData<String>
        get() = _message

    override suspend fun getTravels(): Response<List<Travel>> {
        return try{
            travelsApiService.allTravels(
                loginDataProvider.credentials.value ?: "",
                loginDataProvider.userUuid!!
            )
        }catch (e: Exception){
            _message.postValue(e.message)
            Response(ResponseType.ERROR, "", null)
        }
    }

    override suspend fun addTravel(): Response<Travel> {
        return try{
            travelsApiService.addTravel(
                loginDataProvider.credentials.value ?: "",
                loginDataProvider.userUuid!!
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