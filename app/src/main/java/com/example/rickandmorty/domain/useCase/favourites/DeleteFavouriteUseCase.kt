package com.example.rickandmorty.domain.useCase.favourites

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.RickAndMortyRepository

class DeleteFavouriteUseCase(private val rickAndMortyRepository: RickAndMortyRepository) {
    suspend operator fun invoke(character: Character) {
        return rickAndMortyRepository.removeFromFavourites(character)
    }
}