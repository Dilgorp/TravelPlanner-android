package ru.dilgorp.android.travelplanner.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    companion object{
        private const val SHARED_PREFS_NAME = "travel_planner_shared_prefs"
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) : SharedPreferences{
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
}