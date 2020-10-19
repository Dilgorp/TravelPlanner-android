package ru.dilgorp.android.travelplanner.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.data.UserRequest
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface SearchApiService {

    @GET(CITY)
    suspend fun getCity(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("cityname") cityname: String
    ): Response<UserRequest>

    @GET(PLACES)
    suspend fun getPlaces(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("uuid") uuid: UUID,
        @Query("pageToken") pageToken: String = ""
    ): Response<List<Place>>

    companion object {
        private const val CITY = "search/city/{cityname}"
        private const val PLACES = "/search/places/city/{uuid}"
    }
}