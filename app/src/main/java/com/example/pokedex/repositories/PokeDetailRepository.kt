package com.example.pokedex.repositories

import com.example.pokedex.data.Chain
import com.example.pokedex.data.Evolution
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.network.ApiResponse
import com.example.pokedex.network.PokeApiService
import retrofit2.Response
import javax.inject.Inject

class PokeDetailRepository @Inject constructor(
    private val apiService: PokeApiService
) {

    suspend fun getPokemonDetail(id: Int): Response<Pokemon> {
        return apiService.getPokemonDetails(id)
    }

    suspend fun getPokemonSpecies(id: Int): Response<PokemonSpecy>{
        return apiService.getPokemonSpecies(id)
    }

    suspend fun getEvolutionChain(url: String): Response<Evolution> {
       return apiService.getPokemonEvolutionChain(url)
    }
}