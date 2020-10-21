package ru.dilgorp.android.travelplanner.network

import retrofit2.http.*
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface CitiesApiService {

    @GET(GET_CITIES_PATH)
    suspend fun getCities(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID
    ): Response<List<City>>

    @POST(ADD_CITY_PATH)
    suspend fun addCity(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID
    ): Response<City>

    @GET(GET_CITY_PATH)
    suspend fun getCity(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID,
        @Path("city_uuid") cityUuid: UUID
    ): Response<City>

    @DELETE(DELETE_CITY_PATH)
    suspend fun deleteCity(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID,
        @Path("city_uuid") cityUuid: UUID
    ): Response<City>

    companion object{
        private const val GET_CITIES_PATH = "/user/{user_uuid}/travel/{travel_uuid}/city/all"
        private const val ADD_CITY_PATH = "/user/{user_uuid}/travel/{travel_uuid}/city/add"
        private const val GET_CITY_PATH = "/user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}"
        private const val DELETE_CITY_PATH =
            "/user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/delete"
    }
}