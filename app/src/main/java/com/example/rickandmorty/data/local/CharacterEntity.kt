package com.example.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    val name: String,
    val avatarUrl: String,
    @PrimaryKey val id: Int? = null
)