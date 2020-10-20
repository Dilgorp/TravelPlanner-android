package ru.dilgorp.android.travelplanner.provider

import androidx.lifecycle.LiveData
import java.util.*

interface LoginDataProvider {
    val credentials: LiveData<String>
    val userUuid: UUID?
    fun logout()
}