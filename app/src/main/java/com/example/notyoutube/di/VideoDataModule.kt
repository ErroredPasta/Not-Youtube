package com.example.notyoutube.di

import com.example.notyoutube.data.remote.FakeVideoApi
import com.example.notyoutube.data.remote.VideoApi
import com.example.notyoutube.data.repository.VideoRepositoryImpl
import com.example.notyoutube.domain.repository.VideoRepository
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface VideoDataModule {
    @Binds
    fun bindVideoRepository(impl: VideoRepositoryImpl): VideoRepository

    companion object {
        @Provides
        @Singleton
        fun provideVideoApi(gson: Gson): VideoApi = FakeVideoApi(gson) /* TODO: change to retrofit create */

        @Provides
        @Singleton
        fun provideGson(): Gson = Gson() /* TODO: delete this */
    }
}