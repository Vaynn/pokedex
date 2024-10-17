package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class PokemonXX(
    @SerializedName("pokemon")
    val pokemon: PokemonXXX,
    @SerializedName("slot")
    val slot: Int
)