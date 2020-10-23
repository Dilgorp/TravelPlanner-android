package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.repository.TravelRepository
import ru.dilgorp.android.travelplanner.vm.AbstractMessageViewModel
import java.util.*
import javax.inject.Inject

class TravelViewModel @Inject constructor(
    private val travelRepository: TravelRepository
) : AbstractMessageViewModel() {

    override val message: LiveData<String>
        get() = travelRepository.message

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>>
        get() = _cities

    private val _city = MutableLiveData<City>()
    val city: LiveData<City>
        get() = _city

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    override fun messageShown() {
        travelRepository.messageShown()
    }

    fun updateCities(travelUuid: UUID) = viewModelScope.launch (Dispatchers.IO){
        startLoading()
        val response =  travelRepository.getCities(travelUuid)
        if(response.type == ResponseType.SUCCESS){
            _cities.postValue(response.data)
        }
        travelRepository.refreshTravel(travelUuid)
        stopLoading()
    }

    fun addCity(travelUuid: UUID) = viewModelScope.launch (Dispatchers.IO) {
        startLoading()
        val response = travelRepository.addCity(travelUuid)
        if(response.type == ResponseType.SUCCESS){
            _city.postValue(response.data)
        }
        stopLoading()
    }

    fun deleteCity(city: City)  = viewModelScope.launch (Dispatchers.IO) {
        startLoading()
        val response = travelRepository.deleteCity(city)
        if(response.type == ResponseType.SUCCESS){
            _cities.postValue(response.data)
        }
        travelRepository.refreshTravel(city.travelUuid)
        stopLoading()
    }

    fun selectCity(city: City){
        _city.value = city
    }

    fun navigateDone(){
        _city.value = null
    }

    private fun startLoading(){
        _loading.postValue(true)
    }

    private fun stopLoading() {
        _loading.postValue(false)
    }
}