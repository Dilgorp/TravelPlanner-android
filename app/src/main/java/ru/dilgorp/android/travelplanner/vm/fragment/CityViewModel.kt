package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.CityPlace
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.CityRepository
import ru.dilgorp.android.travelplanner.vm.AbstractMessageViewModel
import javax.inject.Inject

class CityViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    loginDataProvider: LoginDataProvider
) : AbstractMessageViewModel() {

    override val message: LiveData<String>
        get() = cityRepository.message

    private val _searching = MutableLiveData<Boolean>()
    val searching: LiveData<Boolean>
        get() = _searching

    private val _city = MutableLiveData<City>()
    val city: LiveData<City>
        get() = _city

    private val _places = MutableLiveData<List<CityPlace>>()
    val places: LiveData<List<CityPlace>>
        get() = _places

    val credentials = loginDataProvider.credentials

    override fun messageShown() {
        cityRepository.messageShown()
    }

    fun getCityInfo(city: City, cityname: String) {
        _searching.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = cityRepository.getCityInfo(city, cityname).data
            if (result != null) {
                _city.postValue(result)
            }
            _searching.postValue(false)
        }
    }

    fun getCityPlaces(city: City) {
        _searching.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = cityRepository.getCityPlaces(city).data
            if (result != null) {
                _places.postValue(result)
            }
            _searching.postValue(false)
        }
    }

    fun setMessage(messageString: String?) {
        cityRepository.setMessage(messageString ?: "")
    }
}