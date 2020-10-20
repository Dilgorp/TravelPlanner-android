package ru.dilgorp.android.travelplanner.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.dilgorp.android.travelplanner.MainActivity
import ru.dilgorp.android.travelplanner.ui.fragments.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        ProviderModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(cityFragment: CityFragment)
    fun inject(placesFragment: PlacesFragment)
    fun inject(travelsFragment: TravelsFragment)
    fun inject(travelFragment: TravelFragment)
}