package ru.dilgorp.android.travelplanner

import android.app.Application
import ru.dilgorp.android.travelplanner.di.AppComponent
import ru.dilgorp.android.travelplanner.di.DaggerAppComponent
import ru.dilgorp.android.travelplanner.provider.AppComponentProvider

class TravelPlannerApplication : Application(), AppComponentProvider {
    private val appComponent = DaggerAppComponent.factory().create(this)

    override fun getAppComponent(): AppComponent {
        return appComponent
    }
}