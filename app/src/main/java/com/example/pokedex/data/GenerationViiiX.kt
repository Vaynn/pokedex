package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class GenerationViiiX(
    @SerializedName("brilliant-diamond-and-shining-pearl")
    val brilliantDiamondAndShiningPearl: BrilliantDiamondAndShiningPearl,
    @SerializedName("legends-arceus")
    val legendsArceus: LegendsArceus,
    @SerializedName("sword-shield")
    val swordShield: SwordShield
)