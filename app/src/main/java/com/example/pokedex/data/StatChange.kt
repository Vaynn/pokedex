package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class StatChange(
    @SerializedName("change")
    val change: Int,
    @SerializedName("stat")
    val stat: StatXX
)