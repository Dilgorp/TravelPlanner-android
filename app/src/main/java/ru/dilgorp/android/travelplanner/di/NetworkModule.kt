package ru.dilgorp.android.travelplanner.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.dilgorp.android.travelplanner.BuildConfig
import ru.dilgorp.android.travelplanner.network.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        const val BASE_URL_NAME = "ru.dilgorp.android.travelplanner.base_url"
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
    }

    @Provides
    @Singleton
    @Named(BASE_URL_NAME)
    fun provideBaseUrl(): String {
        return BuildConfig.LOCAL_SERVER
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideUuidAdapter(): UuidAdapter {
        return UuidAdapter()
    }

    @Provides
    fun provideMoshi(uuidAdapter: UuidAdapter): Converter.Factory {
        return MoshiConverterFactory.create(
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(uuidAdapter)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: Converter.Factory,
        @Named(BASE_URL_NAME) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationApiService(retrofit: Retrofit): AuthenticationApiService {
        return retrofit.create(AuthenticationApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTravelsApiService(retrofit: Retrofit): TravelsApiService {
        return retrofit.create(TravelsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCitiesApiService(retrofit: Retrofit): CitiesApiService {
        return retrofit.create(CitiesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCityPlacesApiService(retrofit: Retrofit): CityPlacesApiService {
        return retrofit.create(CityPlacesApiService::class.java)
    }
}