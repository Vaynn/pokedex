package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class EvolvesToX(
    @SerializedName("evolution_details")
    val evolutionDetails: List<EvolutionDetailX>,
    @SerializedName("evolves_to")
    val evolvesTo: List<Any>,
    @SerializedName("is_baby")
    val isBaby: Boolean,
    @SerializedName("species")
    val species: SpeciesXXX
)