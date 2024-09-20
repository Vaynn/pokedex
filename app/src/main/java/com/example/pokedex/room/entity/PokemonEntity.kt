package com.example.pokedex.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val spriteUrl: String,
    val generation: Int,
    val type1: String,
    val type2: String?
)
