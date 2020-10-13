package ru.dilgorp.android.travelplanner.repository

interface LoginRepository : MessageRepository {
    suspend fun login(username: String, password: String)
    suspend fun register(username: String, password: String)
}