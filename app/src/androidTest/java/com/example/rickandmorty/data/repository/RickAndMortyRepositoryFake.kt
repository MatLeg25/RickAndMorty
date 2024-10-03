package com.example.rickandmorty.data.repository

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharactersData
import com.example.rickandmorty.domain.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RickAndMortyRepositoryFake(characterNamePrefix: String) : RickAndMortyRepository {

    companion object Config {
        val TOTAL_PAGES = 3
        val PER_PAGE = 10
    }

    var shouldReturnError = false

    private val perPage = Config.PER_PAGE
    private val totalPages = Config.TOTAL_PAGES

    lateinit var characters: List<Character>
        private set

    private val _favourites = MutableStateFlow<List<Character>>(emptyList())
    val favouritesFlow: StateFlow<List<Character>> = _favourites


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
        return Result.success(favouritesFlow.value)
    }

    override fun getFavouritesFlow(): Flow<List<Character>> {
        return favouritesFlow
    }

    override suspend fun addToFavourites(character: Character) {
        _favourites.value += character.copy(isFavourite = true)
    }

    override suspend fun removeFromFavourites(character: Character) {
        _favourites.value = _favourites.value.filterNot { it.id == character.id }
    }

}