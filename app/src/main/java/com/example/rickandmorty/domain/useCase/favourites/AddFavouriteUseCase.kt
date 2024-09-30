package com.example.rickandmorty.domain.useCase.favourites

import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import com.example.rickandmorty.domain.model.Character

class AddFavouriteUseCase(private val rickAndMortyRepository: RickAndMortyRepository) {
    suspend operator fun invoke(character: Character) {
        return rickAndMortyRepository.addToFavourites(character)
    }
}