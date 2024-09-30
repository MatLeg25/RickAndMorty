package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharactersData

interface RickAndMortyRepository {
    suspend fun getCharacters(page: Int): Result<CharactersData>
    suspend fun getFavourites(): Result<List<Character>>
    suspend fun addToFavourites(character: Character)
    suspend fun removeFromFavourites(character: Character)
}