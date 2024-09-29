package com.example.rickandmorty.di

import com.example.rickandmorty.data.remote.RickAndMortyApi
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRickAndMortyApi(): RickAndMortyApi {
        return RickAndMortyApi.create()
    }

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(repository: RickAndMortyRepository): GetCharactersUseCase {
        return GetCharactersUseCase(repository)
    }

}