package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class TypeXXX(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)