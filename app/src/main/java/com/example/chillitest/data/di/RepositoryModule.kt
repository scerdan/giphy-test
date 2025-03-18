package com.example.chillitest.data.di

import com.example.chillitest.data.repository.GiphyRepository
import com.example.chillitest.data.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGiphyRepository(api: ApiService): GiphyRepository {
        return GiphyRepository(api)
    }
}