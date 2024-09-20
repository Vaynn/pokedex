package com.example.pokedex.models

import com.example.pokedex.data.Sprites

interface PokemonPreview {
    val id: Int
    val name: String
    val order: Int
    val fakeUrl: String
}