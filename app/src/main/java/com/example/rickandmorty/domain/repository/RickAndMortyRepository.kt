package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character

interface RickAndMortyRepository {
    suspend fun getCharacters(page: Int): Result<List<Character>>
}