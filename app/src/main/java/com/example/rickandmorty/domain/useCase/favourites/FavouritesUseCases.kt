package com.example.rickandmorty.domain.useCase.favourites

class FavouritesUseCases(
    val getFavouritesUseCase: GetFavouritesUseCase,
    val addFavouriteUseCase: AddFavouriteUseCase,
    val deleteFavouriteUseCase: DeleteFavouriteUseCase
)