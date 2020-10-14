package ru.dilgorp.android.travelplanner.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.dilgorp.android.travelplanner.network.response.CitySearchResponse

interface SearchApiService {

    @GET(CITY)
    suspend fun getCity(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("cityname") cityname: String
    ): CitySearchResponse

    companion object {
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val CITY = "search/city/{cityname}"
    }
}