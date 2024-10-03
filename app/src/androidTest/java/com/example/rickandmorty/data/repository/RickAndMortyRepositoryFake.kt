package com.example.rickandmorty.data.repository

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharactersData
import com.example.rickandmorty.domain.repository.RickAndMortyRepository

class RickAndMortyRepositoryFake(characterNamePrefix: String) : RickAndMortyRepository {

    companion object Config {
        val TOTAL_PAGES = 3
        val PER_PAGE = 10
    }

    var shouldReturnError = false

    private val perPage = Config.PER_PAGE
    private val totalPages = Config.TOTAL_PAGES
    private val lock = Any()

    private var favourites = emptyList<Character>()
    lateinit var characters: List<Character>
        private set


    init {
        generateCharacters(namePrefix = characterNamePrefix)
    }

    private fun generateCharacters(namePrefix: String) {
        characters = List(30) { index ->
            Character(
                id = index,
                name = "$namePrefix$index",
                avatarUrl = "",
                isFavourite = false
            )
        }
    }

    override suspend fun getCharacters(page: Int): Result<CharactersData> {
        return if (shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(
                CharactersData(
                    totalPages = totalPages,
                    characters = characters
                )
            )
        }
    }

    override suspend fun getFavourites(): Result<List<Character>> {
        return Result.success(favourites)
    }

    override suspend fun addToFavourites(character: Character) {

        synchronized(lock) {
            favourites = favourites + character.copy(isFavourite = true)
            println(">>>>>addToFavourites $favourites")
        }
    }

    override suspend fun removeFromFavourites(character: Character) {
        synchronized(lock) {
            favourites = favourites.filterNot { it.id == character.id }
            println(">>>>>removeFromFavourites $favourites")
        }
    }

}