package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class Generation(
    @SerializedName("abilities")
    val abilities: List<Any>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main_region")
    val mainRegion: MainRegion,
    @SerializedName("moves")
    val moves: List<MoveXX>,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<Name>,
    @SerializedName("pokemon_species")
    val pokemonSpecies: List<Species>,
    @SerializedName("types")
    val types: List<TypeXX>,
    @SerializedName("version_groups")
    val versionGroups: List<VersionGroupX>
)