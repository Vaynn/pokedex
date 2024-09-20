package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class SpeciesXXX(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)