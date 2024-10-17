package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class FormName(
    @SerializedName("language")
    val language: LanguageXXXX,
    @SerializedName("name")
    val name: String
)