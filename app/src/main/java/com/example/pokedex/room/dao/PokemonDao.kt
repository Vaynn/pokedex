package com.example.pokedex.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.room.entity.PokemonEntity

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_table WHERE generation = :generation LIMIT :limit OFFSET :offset")
    suspend fun getPokemonByGeneration(generation: Int, limit: Int, offset: Int): List<PokemonEntity>

}