package ru.dilgorp.android.travelplanner.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.data.UserRequest
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface SearchApiService {

    @GET(SEARCH_CITY_PATH)
    suspend fun getCity(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID,
        @Path("city_uuid") cityUuid: UUID,
        @Path("cityname") cityname: String
    ): Response<City>

    @GET(SEARCH_CITY_PLACES_PATH)
    suspend fun getPlaces(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("request_uuid") uuid: UUID
    ): Response<List<Place>>

    companion object {
        private const val SEARCH_CITY_PATH =
            "user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/search/{cityname}"
        private const val SEARCH_CITY_PLACES_PATH = "search/places/{request_uuid}"
    }
}