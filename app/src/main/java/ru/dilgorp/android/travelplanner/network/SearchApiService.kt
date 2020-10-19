package ru.dilgorp.android.travelplanner.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.network.SearchApiService.Companion.PLACES
import ru.dilgorp.android.travelplanner.network.response.CitySearchResponse
import ru.dilgorp.android.travelplanner.network.response.PlaceSearchResponse
import java.util.*

interface SearchApiService {

    @GET(CITY)
    suspend fun getCity(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("cityname") cityname: String
    ): CitySearchResponse

    @GET(PLACES)
    suspend fun getPlaces(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("uuid") uuid: UUID,
        @Query("pageToken") pageToken: String = ""
    ): PlaceSearchResponse

    companion object {
        private const val CITY = "search/city/{cityname}"
        private const val PLACES = "/search/places/city/{uuid}"
    }
}