package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class PaginatedPokemonList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<PokemonShort>
)