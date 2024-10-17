package com.example.pokedex.repositories

import com.example.pokedex.data.Chain
import com.example.pokedex.data.Evolution
import com.example.pokedex.data.MoveDetail
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonForm
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.models.PokemonMove
import com.example.pokedex.network.ApiResponse
import com.example.pokedex.network.PokeApiService
import com.example.pokedex.room.dao.PokemonDao
import com.example.pokedex.room.entity.PokemonEntity
import retrofit2.Response
import javax.inject.Inject

class PokeDetailRepository @Inject constructor(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao
) {

    suspend fun getPokemonDetail(id: Int): Response<Pokemon> {
        return apiService.getPokemonDetails(id)
    }

    suspend fun getPokemonDetailByNameApi(name: String): Response<Pokemon> {
        return apiService.getPokemonDetailsByName(name)
    }

    suspend fun getPokemonByNameDao(name: String): PokemonEntity?{
        return pokemonDao.getPokemonByName(name)
    }

    suspend fun getPokemonByIdDao(id: Int): PokemonEntity?{
        return pokemonDao.getPokemonById(id)
    }

    suspend fun getPokemonDetailByUrl(url: String): Response<Pokemon>{
        return apiService.getPokemonDetailsByUrl(url)
    }

    suspend fun getPokemonSpecies(id: Int): Response<PokemonSpecy>{
        return apiService.getPokemonSpecies(id)
    }

    suspend fun getPokemonSpeciesByName(name: String): Response<PokemonSpecy>{
        return apiService.getPokemonSpeciesByName(name)
    }

    suspend fun getEvolutionChain(url: String): Response<Evolution> {
       return apiService.getPokemonEvolutionChain(url)
    }

    suspend fun getMove(url: String): Response<MoveDetail>{
      return apiService.getPokemonMove(url)
    }

    suspend fun getPokemonForm(url: String): Response<PokemonForm>{
        return apiService.getPokemonForm(url)
    }
}