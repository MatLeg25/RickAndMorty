package com.example.rickandmorty.domain.useCase

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.RickAndMortyRepository

class GetCharactersUseCase(private val rickAndMortyRepository: RickAndMortyRepository) {
    suspend operator fun invoke(page: Int): Result<List<Character>> {
        return rickAndMortyRepository.getCharacters(page)
    }
}