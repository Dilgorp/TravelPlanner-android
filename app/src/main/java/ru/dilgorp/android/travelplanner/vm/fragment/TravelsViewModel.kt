package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.repository.TravelRepository
import ru.dilgorp.android.travelplanner.vm.AbstractMessageViewModel

class TravelsViewModel(
    private val travelRepository: TravelRepository
) : AbstractMessageViewModel() {

    private val _travels = MutableLiveData<List<Travel>>()
    val travels: LiveData<List<Travel>>
        get() = _travels

    private val _travel = MutableLiveData<Travel>()
    val travel: LiveData<Travel>
        get() = _travel

    override val message: LiveData<String>
        get() = travelRepository.message

    override fun messageShown() {
        travelRepository.messageShown()
    }

    fun updateTravels() = viewModelScope.launch(Dispatchers.IO) {
        val response = travelRepository.getTravels()
        if (response.type == ResponseType.SUCCESS) {
            _travels.postValue(response.data)
        }
    }

    fun addTravel() = viewModelScope.launch(Dispatchers.IO) {
        val response = travelRepository.addTravel()
        if(response.type == ResponseType.SUCCESS){
            _travel.postValue(response.data)
        }
    }

    fun selectTravel(travel: Travel){
        _travel.value = travel
    }
}