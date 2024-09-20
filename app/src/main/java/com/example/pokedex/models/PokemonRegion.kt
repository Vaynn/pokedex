package com.example.pokedex.models

import androidx.annotation.DrawableRes
import com.example.pokedex.data.PokemonShort

/**
 * Data class for region and first and last pokemon of this region
 */
data class PokemonRegion(
    val region: String,
    val firstPokemonId: Int,
    val lastPokemonId: Int,
    @DrawableRes
    val regionIcon: Int
)
