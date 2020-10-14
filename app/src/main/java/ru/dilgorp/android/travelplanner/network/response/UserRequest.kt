package ru.dilgorp.android.travelplanner.network.response

class UserRequest(
    val uuid: String,
    val text: String,
    val formattedAddress: String,
    val name: String
)