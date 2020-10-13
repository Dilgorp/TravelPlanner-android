package ru.dilgorp.android.travelplanner.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.dilgorp.android.travelplanner.network.AuthenticationApiService
import ru.dilgorp.android.travelplanner.network.UuidAdapter
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val BASE_URL = "http://192.168.1.55:8080/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
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
        moshiConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationApiService(retrofit: Retrofit): AuthenticationApiService {
        return retrofit.create(AuthenticationApiService::class.java)
    }
}