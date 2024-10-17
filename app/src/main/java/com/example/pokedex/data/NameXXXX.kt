package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class NameXXXX(
    @SerializedName("language")
    val language: LanguageXXXX,
    @SerializedName("name")
    val name: String
)