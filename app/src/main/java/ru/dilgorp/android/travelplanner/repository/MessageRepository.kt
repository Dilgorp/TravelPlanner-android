package ru.dilgorp.android.travelplanner.repository

import androidx.lifecycle.LiveData

interface MessageRepository {
    val message: LiveData<String>
    fun messageShown()
}