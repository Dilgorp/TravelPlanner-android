package ru.dilgorp.android.travelplanner.network.response

abstract class Response(
    val type: ResponseType,
    val message: String
)