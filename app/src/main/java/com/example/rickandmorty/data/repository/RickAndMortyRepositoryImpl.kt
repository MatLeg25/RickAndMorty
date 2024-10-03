package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.local.RickAndMortyDatabase
import com.example.rickandmorty.data.mapper.mapToDomainModelFlow
import com.example.rickandmorty.data.mapper.toDomainModel
import com.example.rickandmorty.data.mapper.toEntity
import com.example.rickandmorty.data.remote.RickAndMortyApi
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharactersData
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val db: RickAndMortyDatabase
) : RickAndMortyRepository {

    private val dao = db.dao

    override suspend fun getCharacters(page: Int): Result<CharactersData> {
        return try {
            val response = api.getCharacters(page = page).toDomainModel()
            val domainData = setFavourites(response)
            Result.success(domainData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavourites(): Result<List<Character>> {
        return try {
            val data = dao.getAllCharacters().take(1000).map { it.toDomainModel() }
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //todo consider pagination
    override fun getFavouritesFlow(): Flow<List<Character>> {
        return dao.getAllCharactersFlow().mapToDomainModelFlow()
    }

    override suspend fun addToFavourites(character: Character) {
        dao.insertCharacter(character.toEntity())
    }

    override suspend fun removeFromFavourites(character: Character) {
        dao.deleteCharacter(character.toEntity())
    }

    /**
     * Set isFavourite state based on local DB data
     */
    private suspend fun setFavourites(data: CharactersData): CharactersData {
        val favouritesIds = dao.getAllCharacters().map { it.id }
        val map: Map<Boolean, List<Character>> = data.characters.groupBy { it.id in favouritesIds }
        val favourites = map[true]?.map { it.copy(isFavourite = true) } ?: emptyList()
        val noFavourites = map[false]?.map { it.copy(isFavourite = false) } ?: emptyList()
        return data.copy(characters = (favourites + noFavourites).sortedBy { it.id })
    }

}