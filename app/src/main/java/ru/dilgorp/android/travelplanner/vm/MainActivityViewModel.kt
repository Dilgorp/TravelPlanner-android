package ru.dilgorp.android.travelplanner.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val loginDataProvider: LoginDataProvider
) : AbstractMessageViewModel(){

    private val _message = MutableLiveData<String>()
    override val message: LiveData<String>
        get() = _message

    val credentials = loginDataProvider.credentials

    override fun messageShown() {
        _message.value = ""
    }

    fun logout(){
        loginDataProvider.logout()
    }
}