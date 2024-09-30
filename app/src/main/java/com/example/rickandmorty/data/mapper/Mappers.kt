package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.local.CharacterEntity
import com.example.rickandmorty.data.remote.dto.RickAndMortyResponse
import com.example.rickandmorty.domain.model.Character

fun RickAndMortyResponse.toDomainModel(): List<Character> =
    this.results.map {
        Character(id = it.id, name = it.name, avatarUrl = it.url)
    }

fun CharacterEntity.toDomainModel(): Character =
    Character(id = this.id!!, name = this.name, avatarUrl = this.avatarUrl)

fun Character.toEntity(): CharacterEntity =
    CharacterEntity(id = this.id, name = this.name, avatarUrl = this.avatarUrl)