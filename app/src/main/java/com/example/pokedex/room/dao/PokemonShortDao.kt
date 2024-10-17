package com.example.pokedex.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.room.entity.PokemonShortEntity

@Dao
interface PokemonShortDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemonShort: List<PokemonShortEntity>)

    @Query("SELECT * FROM pokemon_short_table")
    suspend fun getAllPokemonShort(): List<PokemonShortEntity>

    @Query("SELECT COUNT(*) FROM pokemon_short_table")
    suspend fun getCount(): Int

    @Query("SELECT * FROM pokemon_short_table WHERE name LIKE '%' || :query || '%'")
    suspend fun searchPokemonByName(query: String): List<PokemonShortEntity>
}