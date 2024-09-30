package com.example.rickandmorty.di

import android.app.Application
import androidx.room.Room
import com.example.rickandmorty.data.local.RickAndMortyDatabase
import com.example.rickandmorty.data.remote.RickAndMortyApi
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import com.example.rickandmorty.domain.useCase.favourites.AddFavouriteUseCase
import com.example.rickandmorty.domain.useCase.favourites.DeleteFavouriteUseCase
import com.example.rickandmorty.domain.useCase.favourites.FavouritesUseCases
import com.example.rickandmorty.domain.useCase.favourites.GetFavouritesUseCase
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
    fun provideRickAndMortyDatabase(application: Application): RickAndMortyDatabase {
        return Room.databaseBuilder(
            application,
            RickAndMortyDatabase::class.java,
            "rickandmortydb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(repository: RickAndMortyRepository): GetCharactersUseCase {
        return GetCharactersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavouritesUseCase(repository: RickAndMortyRepository): GetFavouritesUseCase {
        return GetFavouritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddFavouriteUseCase(repository: RickAndMortyRepository): AddFavouriteUseCase {
        return AddFavouriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteFavouriteUseCase(repository: RickAndMortyRepository): DeleteFavouriteUseCase {
        return DeleteFavouriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFavouritesUseCases(
        getFavouritesUseCase: GetFavouritesUseCase,
        addFavouriteUseCase: AddFavouriteUseCase,
        deleteFavouriteUseCase: DeleteFavouriteUseCase
    ): FavouritesUseCases {
        return FavouritesUseCases(
            getFavouritesUseCase = getFavouritesUseCase,
            addFavouriteUseCase = addFavouriteUseCase,
            deleteFavouriteUseCase = deleteFavouriteUseCase,
        )
    }


}