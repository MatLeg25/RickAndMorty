package com.example.rickandmorty.di

import com.example.rickandmorty.data.repository.RickAndMortyRepositoryImpl
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRickAndMortyRepository(repository: RickAndMortyRepositoryImpl): RickAndMortyRepository

}