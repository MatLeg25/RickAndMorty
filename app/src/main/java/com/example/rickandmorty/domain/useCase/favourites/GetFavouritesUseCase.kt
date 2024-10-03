package com.example.rickandmorty.domain.useCase.favourites

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.Flow

class GetFavouritesUseCase(private val rickAndMortyRepository: RickAndMortyRepository) {
    operator fun invoke(): Flow<List<Character>> {
        return rickAndMortyRepository.getFavouritesFlow()
    }
}


