package ru.dilgorp.android.travelplanner.di

import dagger.Binds
import dagger.Module
import ru.dilgorp.android.travelplanner.repository.CityRepository
import ru.dilgorp.android.travelplanner.repository.LoginRepository
import ru.dilgorp.android.travelplanner.repository.PlacesRepository
import ru.dilgorp.android.travelplanner.repository.impl.CityRepositoryImpl
import ru.dilgorp.android.travelplanner.repository.impl.LoginRepositoryImpl
import ru.dilgorp.android.travelplanner.repository.impl.PlacesRepositoryImpl

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindLoginRepository(loginRepository: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindCityRepository(cityRepository: CityRepositoryImpl): CityRepository

    @Binds
    abstract fun bindPlacesRepository(placesRepository: PlacesRepositoryImpl): PlacesRepository
}