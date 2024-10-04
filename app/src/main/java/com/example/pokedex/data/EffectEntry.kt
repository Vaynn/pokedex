package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class EffectEntry(
    @SerializedName("effect")
    val effect: String,
    @SerializedName("language")
    val language: LanguageXX,
    @SerializedName("short_effect")
    val shortEffect: String
)