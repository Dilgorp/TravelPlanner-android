package ru.dilgorp.android.travelplanner.network.response

class Response<T>(
    val type: ResponseType,
    val message: String?,
    val data: T?
)