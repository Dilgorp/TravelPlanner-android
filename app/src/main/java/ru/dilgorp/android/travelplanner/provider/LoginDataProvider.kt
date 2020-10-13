package ru.dilgorp.android.travelplanner.provider

import androidx.lifecycle.LiveData

interface LoginDataProvider {
    val credentials: LiveData<String>
    fun logout()
}