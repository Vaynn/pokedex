package com.example.pokedex.models


import com.example.pokedex.data.PokemonShort
import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("results")
    val results: List<PokemonShort>
)
