package ru.dilgorp.android.travelplanner.di

import dagger.Binds
import dagger.Module
import ru.dilgorp.android.travelplanner.provider.LoginDataProvider
import ru.dilgorp.android.travelplanner.repository.impl.LoginRepositoryImpl

@Module
abstract class ProviderModule {

    @Binds
    abstract fun bindLoginDataProvider(loginRepository: LoginRepositoryImpl): LoginDataProvider
}