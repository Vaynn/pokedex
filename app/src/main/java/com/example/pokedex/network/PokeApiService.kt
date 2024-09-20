package com.example.pokedex.network

import com.example.pokedex.data.Evolution
import com.example.pokedex.data.Generation
import com.example.pokedex.data.PaginatedPokemonList
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.data.Species
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PaginatedPokemonList>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): Response<Pokemon>

    @GET
    suspend fun getPokemonDetailsByUrl(@Url url: String): Response<Pokemon>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): Response<PokemonSpecy>

    @GET("generation/{id}")
    suspend fun getPokemonGeneration(@Path("id") id: Int): Response<Generation>

    @GET
    suspend fun getPokemonEvolutionChain(@Url url: String): Response<Evolution>

}