package ru.dilgorp.android.travelplanner.network.response

class AuthenticationResponse(
    type: ResponseType,
    message: String
) : Response(type, message)