package ru.dilgorp.android.travelplanner.network

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.network.response.AuthenticationResponse

interface AuthenticationApiService {

    @POST(REGISTRATION)
    suspend fun postRegistration(
        @Body requestBody: RequestBody
    ): AuthenticationResponse

    @GET(LOGIN)
    suspend fun getLogin(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String
    ): AuthenticationResponse

    companion object{
        private const val REGISTRATION = "registration"
        private const val LOGIN = "login"
    }
}