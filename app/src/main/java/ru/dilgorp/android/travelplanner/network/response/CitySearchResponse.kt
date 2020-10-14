package ru.dilgorp.android.travelplanner.network.response

class CitySearchResponse(
    type: ResponseType, message: String, val userRequest: UserRequest?
) : Response(type, message)