package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.local.CharacterEntity
import com.example.rickandmorty.data.remote.dto.RickAndMortyResponse
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharactersData

fun RickAndMortyResponse.toDomainModel(): CharactersData =
    CharactersData(
        totalPages = this.info.pages,
        characters = this.results.map {
            Character(id = it.id, name = it.name, avatarUrl = it.image, isFavourite = false)
        }
    )

fun CharacterEntity.toDomainModel(): Character =
    Character(id = this.id!!, name = this.name, avatarUrl = this.avatarUrl, isFavourite = true)

fun Character.toEntity(): CharacterEntity =
    CharacterEntity(id = this.id, name = this.name, avatarUrl = this.avatarUrl)