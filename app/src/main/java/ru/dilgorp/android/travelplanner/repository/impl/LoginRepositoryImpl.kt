package ru.dilgorp.android.travelplanner.repository.impl

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.Credentials
import ru.dilgorp.android.travelplanner.data.User
import ru.dilgorp.android.travelplanner.network.AuthenticationApiService
import ru.dilgorp.android.travelplanner.network.response.Response
import ru.dilgorp.android.travelplanner.network.response.ResponseType
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.LoginRepository
import java.util.*
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

    private var _userUuid: UUID? = null
    override val userUuid: UUID?
        get() = _userUuid

    init {
        _credentials.value = sharedPrefs.getString(CREDENTIALS_NAME, "") ?: ""
        val uuidString = sharedPrefs.getString(USER_UUID_NAME, null)
        uuidString?.let {
            _userUuid = UUID.fromString(it)
        }
    }

    override suspend fun login(username: String, password: String) {
        try {
            val response =
                authenticationApiService.getLogin(
                    getCredentials(username, password),
                    User(UUID.randomUUID(), username, password)
                )
            processAuthenticationResponse(response, username, password)
        } catch (e: Exception) {
            _message.postValue(e.localizedMessage)
        }
    }

    override suspend fun register(username: String, password: String) {
        try {
            val response =
                authenticationApiService.postRegistration(User(UUID.randomUUID(), username, password))
            processAuthenticationResponse(response, username, password)
        } catch (e: Exception) {
            _message.postValue(e.localizedMessage)
        }
    }

    override fun logout() {
        val editor = sharedPrefs.edit()
        editor.remove(CREDENTIALS_NAME)
        editor.apply()
        _credentials.value = ""
    }

    override fun messageShown() {
        setMessage("")
    }

    override fun setMessage(messageString: String) {
        _message.value = messageString
    }

    private fun processAuthenticationResponse(
        response: Response<User>,
        username: String,
        password: String
    ) {
        when (response.type) {
            ResponseType.SUCCESS -> {
                val credentialsString = getCredentials(username, password)
                val editor = sharedPrefs.edit()
                editor.putString(CREDENTIALS_NAME, credentialsString)
                editor.putString(USER_UUID_NAME, response.data!!.uuid.toString())
                editor.apply()
                _credentials.postValue(credentialsString)
                _userUuid = response.data.uuid
            }
            ResponseType.ERROR -> throw IllegalStateException(response.message)
        }
    }

    private fun getCredentials(username: String, password: String): String {
        return Credentials.basic(username, password, Charsets.UTF_8)
    }

    companion object {
        private const val CREDENTIALS_NAME = "credentials"
        private const val USER_UUID_NAME = "user_uuid"
    }
}