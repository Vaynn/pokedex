package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class Genera(
    @SerializedName("genus")
    val genus: String,
    @SerializedName("language")
    val language: LanguageX
)