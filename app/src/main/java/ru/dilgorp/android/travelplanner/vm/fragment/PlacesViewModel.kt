package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    override fun messageShown() {
        placesRepository.messageShown()
    }

    fun refreshPlaces(cityUUID: UUID) = viewModelScope.launch(Dispatchers.IO) {
        placesRepository.refreshPlaces(cityUUID)
    }
}