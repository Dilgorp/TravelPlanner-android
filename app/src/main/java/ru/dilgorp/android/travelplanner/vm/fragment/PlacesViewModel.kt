package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.PlacesRepository
import ru.dilgorp.android.travelplanner.vm.AbstractMessageViewModel
import java.util.*
import javax.inject.Inject

class PlacesViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    loginDataProvider: LoginDataProvider
) : AbstractMessageViewModel() {

    val places = placesRepository.places
    val credentials = loginDataProvider.credentials.value ?: ""

    override val message: LiveData<String>
        get() = placesRepository.message

    private val _navigation = MutableLiveData<Boolean>()
    val navigation: LiveData<Boolean>
        get() = _navigation

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    override fun messageShown() {
        placesRepository.messageShown()
    }

    fun refreshPlaces(cityUUID: UUID) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        placesRepository.refreshPlaces(cityUUID)
        stopLoading()
    }

    fun addPlaces(city: City, selectedPlaces: List<Place>) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        val response = placesRepository.addPlaces(city, selectedPlaces)
        if(response.type == ResponseType.SUCCESS){
            startNavigation()
        }
        stopLoading()
    }

    private fun startNavigation(){
        _navigation.postValue(true)
    }

    fun navigationDone(){
        _navigation.value = false
    }

    private fun startLoading(){
        _loading.postValue(true)
    }

    private fun stopLoading() {
        _loading.postValue(false)
    }
}