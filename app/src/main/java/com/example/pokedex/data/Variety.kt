package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class Variety(
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("pokemon")
    val pokemon: PokemonX
)