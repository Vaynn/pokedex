package com.example.pokedex.models

import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonShort

sealed class PokemonGridItem {
    data class PokemonItem(val pokemon: PokemonShort) : PokemonGridItem()
    data class RegionHeader(val regionName: String): PokemonGridItem()
}