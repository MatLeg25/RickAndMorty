package com.example.rickandmorty.data.mappers

import com.example.rickandmorty.data.remote.dto.RickAndMortyResponse
import com.example.rickandmorty.domain.model.Character

fun RickAndMortyResponse.toDomainModel(): List<Character> =
    this.results.map {
        Character(id = it.id, name = it.name, avatarUrl = it.url)
    }