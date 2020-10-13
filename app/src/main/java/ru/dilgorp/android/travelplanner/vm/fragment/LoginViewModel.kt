package ru.dilgorp.android.travelplanner.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.LoginRepository
import ru.dilgorp.android.travelplanner.vm.AbstractMessageViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    loginDataProvider: LoginDataProvider
) : AbstractMessageViewModel() {
    override val message: LiveData<String>
        get() = loginRepository.message

    val credentials = loginDataProvider.credentials

    override fun messageShown() {
        loginRepository.messageShown()
    }

    fun login(username: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        loginRepository.login(username, password)
    }

    fun register(username: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        loginRepository.register(username, password)
    }
}