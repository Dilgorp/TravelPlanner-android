package ru.dilgorp.android.travelplanner.network

import retrofit2.http.*
import ru.dilgorp.android.travelplanner.data.City
import ru.dilgorp.android.travelplanner.data.CityPlace
import ru.dilgorp.android.travelplanner.data.Place
import ru.dilgorp.android.travelplanner.di.NetworkModule
import ru.dilgorp.android.travelplanner.network.response.Response
import java.util.*

interface CityPlacesApiService {

    @GET(GET_PLACES_PATH)
    suspend fun getCityPlaces(
        @Header(NetworkModule.AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID,
        @Path("city_uuid") cityUuid: UUID
    ): Response<List<CityPlace>>

    @POST(ADD_PLACES_PATH)
    suspend fun addCityPlaces(
        @Header(NetworkModule.AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID,
        @Path("city_uuid") cityUuid: UUID,
        @Body places: List<Place>
    ): Response<List<CityPlace>>

    @DELETE(DELETE_PLACE_PATH)
    suspend fun deleteCityPlace(
        @Header(NetworkModule.AUTHORIZATION_HEADER_NAME) credentials: String,
        @Path("user_uuid") userUuid: UUID,
        @Path("travel_uuid") travelUuid: UUID,
        @Path("city_uuid") cityUuid: UUID,
        @Path("place_uuid") placeUuid: UUID
    ): Response<List<CityPlace>>

    companion object {
        private const val GET_PLACES_PATH =
            "user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/places/all"
        private const val ADD_PLACES_PATH =
            "user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/places/add"
        private const val DELETE_PLACE_PATH =
            "user/{user_uuid}/travel/{travel_uuid}/city/{city_uuid}/places/{place_uuid}/delete"
    }
}