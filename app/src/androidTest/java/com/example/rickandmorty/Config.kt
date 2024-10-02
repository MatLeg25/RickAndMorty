package com.example.rickandmorty

import com.example.rickandmorty.domain.model.Character

object Config {

    val TOTAL_PAGES = 3
    val PER_PAGE = 10
    val CHARACTER_NAME_PREFIX = "Character-"

    val CHARACTERS_TEST_LIST = List(30) { index ->
        Character(id = index, name = "$CHARACTER_NAME_PREFIX$index", avatarUrl = "", isFavourite = false)
    }

}