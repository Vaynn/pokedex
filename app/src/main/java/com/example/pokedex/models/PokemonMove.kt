package com.example.pokedex.models

data class PokemonMove(
    val name: String,
    val type: String,
    val description: String,
    val pp: Int,
    val power: Int?,
    val levelLearned: Int,
)
