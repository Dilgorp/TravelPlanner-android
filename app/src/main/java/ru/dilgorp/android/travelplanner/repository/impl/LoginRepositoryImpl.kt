package ru.dilgorp.android.travelplanner.repository.impl

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.Credentials
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.dilgorp.android.travelplanner.network.AuthenticationApiService
import ru.dilgorp.android.travelplanner.network.response.AuthenticationResponse
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val authenticationApiService: AuthenticationApiService,
    private val sharedPrefs: SharedPreferences
) : LoginRepository, LoginDataProvider {

    private val _message = MutableLiveData<String>()
    override val message: LiveData<String>
        get() = _message

    private val _credentials = MutableLiveData<String>()
    override val credentials: LiveData<String>
        get() = _credentials

    init {
        _credentials.value = sharedPrefs.getString(CREDENTIALS_NAME, "") ?: ""
    }

    override suspend fun login(username: String, password: String) {
        try {
            val response =
                authenticationApiService.getLogin(getCredentials(username, password))
            processAuthenticationResponse(response, username, password)
        } catch (e: Exception) {
            _message.postValue(e.localizedMessage)
        }
    }

    override suspend fun register(username: String, password: String) {
        try {
            val response =
                authenticationApiService.postRegistration(getRequestBody(username, password))
            processAuthenticationResponse(response, username, password)
        } catch (e: Exception) {
            _message.postValue(e.localizedMessage)
        }
    }

    override fun logout() {
        val editor = sharedPrefs.edit()
        editor.putString(CREDENTIALS_NAME, "")
        editor.apply()
        _credentials.value = ""
    }

    override fun messageShown() {
        _message.value = ""
    }

    private fun processAuthenticationResponse(
        response: AuthenticationResponse,
        username: String,
        password: String
    ) {
        when (response.type) {
            ResponseType.SUCCESS -> {
                val credentialsString = getCredentials(username, password)
                val editor = sharedPrefs.edit()
                editor.putString(CREDENTIALS_NAME, credentialsString)
                editor.apply()
                _credentials.postValue(credentialsString)
            }
            ResponseType.ERROR -> throw IllegalStateException(response.message)
        }
    }

    private fun getRequestBody(username: String, password: String): RequestBody {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()
    }

    private fun getCredentials(username: String, password: String): String {
        return Credentials.basic(username, password, Charsets.UTF_8)
    }

    companion object {
        private const val CREDENTIALS_NAME = "credentials"
    }
}