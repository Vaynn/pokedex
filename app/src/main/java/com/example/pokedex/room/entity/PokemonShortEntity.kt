package com.example.pokedex.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_short_table")
data class PokemonShortEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,

)