package ru.dilgorp.android.travelplanner.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.dilgorp.android.travelplanner.data.User
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.network.response.Response

interface AuthenticationApiService {

    @POST(REGISTRATION)
    suspend fun postRegistration(
        @Body user: User
    ): Response<User>

    @GET(LOGIN)
    suspend fun getLogin(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String
    ): Response<User>

    companion object{
        private const val REGISTRATION = "registration"
        private const val LOGIN = "login"
    }
}