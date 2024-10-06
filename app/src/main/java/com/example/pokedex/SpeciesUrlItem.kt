package com.example.pokedex


import com.google.gson.annotations.SerializedName

data class SpeciesUrlItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)