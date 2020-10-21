package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.repository.TravelsRepository
import ru.dilgorp.android.travelplanner.vm.AbstractMessageViewModel
import javax.inject.Inject

class TravelsViewModel @Inject constructor(
    private val travelsRepository: TravelsRepository
) : AbstractMessageViewModel() {

    private val _travels = MutableLiveData<List<Travel>>()
    val travels: LiveData<List<Travel>>
        get() = _travels

    private val _travel = MutableLiveData<Travel>()
    val travel: LiveData<Travel>
        get() = _travel

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    override val message: LiveData<String>
        get() = travelsRepository.message

    override fun messageShown() {
        travelsRepository.messageShown()
    }

    fun updateTravels() = viewModelScope.launch(Dispatchers.IO) {
        val response = travelsRepository.getTravels()
        if (response.type == ResponseType.SUCCESS) {
            _travels.postValue(response.data)
        }
        loadingStopped()
    }

    fun addTravel() = viewModelScope.launch(Dispatchers.IO) {
        val response = travelsRepository.addTravel()
        if (response.type == ResponseType.SUCCESS) {
            _travel.postValue(response.data)
        }
        loadingStopped()
    }

    fun selectTravel(travel: Travel) {
        _travel.value = travel
    }

    fun loadingStarted(){
        _loading.value = true
    }

    fun loadingStopped(){
        _loading.postValue(false)
    }
}