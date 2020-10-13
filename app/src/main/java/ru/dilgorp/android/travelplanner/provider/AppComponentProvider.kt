package ru.dilgorp.android.travelplanner.provider

import ru.dilgorp.android.travelplanner.di.AppComponent

interface AppComponentProvider {
    fun getAppComponent(): AppComponent
}