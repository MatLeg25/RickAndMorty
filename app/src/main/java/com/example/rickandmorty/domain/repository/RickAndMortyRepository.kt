package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharactersData
import kotlinx.coroutines.flow.Flow

interface RickAndMortyRepository {
    suspend fun getCharacters(page: Int): Result<CharactersData>
    suspend fun getFavourites(): Result<List<Character>>
    fun getFavouritesFlow(): Flow<List<Character>>
    suspend fun addToFavourites(character: Character)
    suspend fun removeFromFavourites(character: Character)
}