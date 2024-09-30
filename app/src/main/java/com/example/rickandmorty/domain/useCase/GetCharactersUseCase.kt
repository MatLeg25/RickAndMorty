package com.example.rickandmorty.domain.useCase

import com.example.rickandmorty.domain.model.CharactersData
import com.example.rickandmorty.domain.repository.RickAndMortyRepository

class GetCharactersUseCase(
    private val rickAndMortyRepository: RickAndMortyRepository
) {
    suspend operator fun invoke(page: Int): Result<CharactersData> {
        return rickAndMortyRepository.getCharacters(page)
    }
}