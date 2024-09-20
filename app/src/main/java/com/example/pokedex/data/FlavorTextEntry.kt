package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: LanguageX,
    @SerializedName("version")
    val version: VersionX
)