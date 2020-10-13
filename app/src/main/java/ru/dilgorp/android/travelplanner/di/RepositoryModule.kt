package ru.dilgorp.android.travelplanner.di

import dagger.Binds
import dagger.Module
import ru.dilgorp.android.travelplanner.repository.LoginRepository
import ru.dilgorp.android.travelplanner.repository.impl.LoginRepositoryImpl

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindLoginRepository(loginRepository: LoginRepositoryImpl): LoginRepository
}