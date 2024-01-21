package com.example.notyoutube.di

import com.example.notyoutube.data.remote.FakeVideoApi
import com.example.notyoutube.data.remote.VideoApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VideoDataModule {
    @Provides
    @Singleton
    fun provideVideoApi(gson: Gson): VideoApi = FakeVideoApi(gson) /* TODO: change to retrofit create */

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson() /* TODO: delete this */
}