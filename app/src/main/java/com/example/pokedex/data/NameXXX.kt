package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class NameXXX(
    @SerializedName("language")
    val language: LanguageXXX,
    @SerializedName("name")
    val name: String
)