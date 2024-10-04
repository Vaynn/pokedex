package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class FlavorTextEntryX(
    @SerializedName("flavor_text")
    val flavorText: String,
    @SerializedName("language")
    val language: LanguageXX,
    @SerializedName("version_group")
    val versionGroup: VersionGroupXX
)