package com.example.rickandmorty.domain.useCase.favourites

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.RickAndMortyRepository

class GetFavouritesUseCase(private val rickAndMortyRepository: RickAndMortyRepository) {
    suspend operator fun invoke(): Result<List<Character>> {
        return rickAndMortyRepository.getFavourites()
    }
}


