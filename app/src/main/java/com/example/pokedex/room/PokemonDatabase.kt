package com.example.pokedex.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.room.dao.PokemonDao
import com.example.pokedex.room.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}