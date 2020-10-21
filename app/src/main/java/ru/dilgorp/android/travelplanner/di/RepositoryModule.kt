package ru.dilgorp.android.travelplanner.di

import dagger.Binds
import dagger.Module
import ru.dilgorp.android.travelplanner.repository.*
import ru.dilgorp.android.travelplanner.repository.impl.*

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindLoginRepository(loginRepository: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindCityRepository(cityRepository: CityRepositoryImpl): CityRepository

    @Binds
    abstract fun bindPlacesRepository(placesRepository: PlacesRepositoryImpl): PlacesRepository

    @Binds
    abstract fun bindTravelsRepository(travelsRepository: TravelsRepositoryImpl): TravelsRepository

    @Binds
    abstract fun bindTravelRepository(travelRepository: TravelRepositoryImpl): TravelRepository
}