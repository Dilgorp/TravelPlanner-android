package ru.dilgorp.android.travelplanner.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.dilgorp.android.travelplanner.vm.MainActivityViewModel
import ru.dilgorp.android.travelplanner.vm.ViewModelFactory
import ru.dilgorp.android.travelplanner.vm.ViewModelKey
import ru.dilgorp.android.travelplanner.vm.fragment.*

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CityViewModel::class)
    abstract fun bindCityViewModel(cityViewModel: CityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlacesViewModel::class)
    abstract fun bindPlacesViewModel(placesViewModel: PlacesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TravelsViewModel::class)
    abstract fun bindTravelsViewModel(travelsViewModel: TravelsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TravelViewModel::class)
    abstract fun bindTravelViewModel(travelViewModel: TravelViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}