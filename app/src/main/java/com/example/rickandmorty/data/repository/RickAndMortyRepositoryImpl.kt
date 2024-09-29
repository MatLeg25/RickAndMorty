package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.mappers.toDomainModel
import com.example.rickandmorty.data.remote.RickAndMortyApi
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
): RickAndMortyRepository {

    override suspend fun getCharacters(page: Int): Result<List<Character>> {
        return try {
            Result.success(
                api.getCharacters(page = page).toDomainModel()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}