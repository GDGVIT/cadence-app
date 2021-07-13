package com.dscvit.cadence.di

import android.content.Context
import androidx.room.Room
import com.dscvit.cadence.api.ApiService
import com.dscvit.cadence.db.AlarmDatabase
import com.dscvit.cadence.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideAlarmDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AlarmDatabase::class.java,
        "alarms_db.db"
    ).build()

    @Provides
    @Singleton
    fun provideAlarmDao(
        db: AlarmDatabase
    ) = db.getAlarmDao()
}
