package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private val _photoId = MutableLiveData<String>()
    val photoId: LiveData<String>
        get() = _photoId

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    val credentials = loginDataProvider.credentials

    override fun messageShown() {
        cityRepository.messageShown()
    }

    fun getCityInfo(text: String) {
        _searching.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = cityRepository.getCityInfo(text).data
            if (result == null) {
                _searching.postValue(false)
                return@launch
            }

            _photoId.postValue(result.uuid)
            _name.postValue(result.name)
            _description.postValue(result.formattedAddress)

            _searching.postValue(false)
        }
    }

    fun setMessage(messageString: String?) {
        cityRepository.setMessage(messageString ?: "")
    }
}