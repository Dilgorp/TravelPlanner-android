package ru.dilgorp.android.travelplanner.provider

interface CredentialsProvider {
    fun provideCredentials(): String
}