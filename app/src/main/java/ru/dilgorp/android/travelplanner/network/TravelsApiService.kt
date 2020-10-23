package ru.dilgorp.android.travelplanner.network

import retrofit2.http.*
import ru.dilgorp.android.travelplanner.data.Travel
import ru.dilgorp.android.travelplanner.di.NetworkModule.Companion.AUTHORIZATION_HEADER_NAME
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface TravelsApiService {

    @POST(ADD_TRAVEL_PATH)
    suspend fun addTravel(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID
    ): Response<Travel>

    @GET(GET_TRAVEL_PATH)
    suspend fun getTravel(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID
    ): Response<Travel>

    @DELETE(DELETE_TRAVEL_PATH)
    suspend fun deleteTravel(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID
    ): Response<List<Travel>>

    @POST(REFRESH_TRAVEL_PATH)
    suspend fun refreshTravel(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID
    ): Response<Travel>

    @GET(ALL_TRAVEL_PATH)
    suspend fun allTravels(
        @Header(AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID
    ): Response<List<Travel>>

    companion object {
        private const val ADD_TRAVEL_PATH = "/user/{user_uuid}/travel/add"
        private const val GET_TRAVEL_PATH = "/user/{user_uuid}/travel/{travel_uuid}"
        private const val DELETE_TRAVEL_PATH = "/user/{user_uuid}/travel/{travel_uuid}/delete"
        private const val REFRESH_TRAVEL_PATH = "/user/{user_uuid}/travel/{travel_uuid}/refresh"
        private const val ALL_TRAVEL_PATH = "/user/{user_uuid}/travel/all"
    }
}