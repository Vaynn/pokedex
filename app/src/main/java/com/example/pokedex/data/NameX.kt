package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class NameX(
    @SerializedName("language")
    val language: LanguageX,
    @SerializedName("name")
    val name: String
)