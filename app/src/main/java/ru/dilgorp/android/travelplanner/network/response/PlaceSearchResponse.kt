package ru.dilgorp.android.travelplanner.network.response

import ru.dilgorp.android.travelplanner.data.Place

class PlaceSearchResponse(
    type: ResponseType, message: String, val places: List<Place>
) : Response(type, message)