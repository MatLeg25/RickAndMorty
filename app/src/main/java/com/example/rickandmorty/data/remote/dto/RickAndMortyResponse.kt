package com.example.rickandmorty.data.remote.dto

data class RickAndMortyResponse(
    val info: Info,
    val results: List<Result>
)