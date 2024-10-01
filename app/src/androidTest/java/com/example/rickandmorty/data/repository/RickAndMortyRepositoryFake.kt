package com.example.rickandmorty.data.repository

import com.example.rickandmorty.Config
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharactersData
import com.example.rickandmorty.domain.repository.RickAndMortyRepository

class RickAndMortyRepositoryFake : RickAndMortyRepository {

    var shouldReturnError = false

    private val characters = Config.CHARACTERS_TEST_LIST
    private val perPage = Config.PER_PAGE
    private val totalPages = Config.TOTAL_PAGES
    private val lock = Any()

    private var favourites = emptyList<Character>()


    override suspend fun getCharacters(page: Int): Result<CharactersData> {
        return if (shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(
                CharactersData(
                    totalPages = totalPages,
                    characters = characters //todo pagination .subList(page - 1, page)
                )
            )
        }
    }

    override suspend fun getFavourites(): Result<List<Character>> {
        return Result.success(favourites)
    }

    override suspend fun addToFavourites(character: Character) {
        synchronized(lock) {
            favourites = favourites + character
        }
    }

    override suspend fun removeFromFavourites(character: Character) {
        synchronized(lock) {
            favourites = favourites.filterNot { it.id == character.id }
        }
    }

}