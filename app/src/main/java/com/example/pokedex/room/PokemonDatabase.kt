package com.example.pokedex.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.room.dao.PokemonDao
import com.example.pokedex.room.dao.PokemonShortDao
import com.example.pokedex.room.entity.PokemonEntity
import com.example.pokedex.room.entity.PokemonShortEntity

@Database(entities = [PokemonEntity::class, PokemonShortEntity::class], version = 2, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonShortDao(): PokemonShortDao
}