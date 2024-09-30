package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.local.RickAndMortyDatabase
import com.example.rickandmorty.data.mapper.toDomainModel
import com.example.rickandmorty.data.mapper.toEntity
import com.example.rickandmorty.data.remote.RickAndMortyApi
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val db: RickAndMortyDatabase
) : RickAndMortyRepository {

    private val dao = db.dao

    override suspend fun getCharacters(page: Int): Result<List<Character>> {
        return try {
            Result.success(
                api.getCharacters(page = page).toDomainModel()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //todo pagination
    override suspend fun getFavourites(): Result<List<Character>> {
        return try {
            val data = dao.getAllCharacters().map { it.toDomainModel() }
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addToFavourites(character: Character) {
        dao.insertCharacter(character.toEntity())
    }

    override suspend fun removeFromFavourites(character: Character) {
        dao.deleteCharacter(character.toEntity())
    }

}