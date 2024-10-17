package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class GameIndiceX(
    @SerializedName("game_index")
    val gameIndex: Int,
    @SerializedName("generation")
    val generation: GenerationXXXX
)