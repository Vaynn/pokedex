package com.example.pokedex.repositories

import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.example.pokedex.R
import com.example.pokedex.data.PaginatedPokemonList
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.Species
import com.example.pokedex.models.PokemonRegion
import com.example.pokedex.network.ApiResponse


import com.example.pokedex.network.PokeApiService
import com.example.pokedex.room.dao.PokemonDao
import com.example.pokedex.room.entity.PokemonEntity
import retrofit2.Response
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao
) {

    private val generationMap = mutableMapOf<Int, PokemonRegion>()


    private val regionImageMap: Map<Int, Int> = mapOf(
        0 to R.drawable.ic_default_pokeball,
        1 to R.drawable.ic_kanto_safariball,
        2 to R.drawable.ic_johto_fastball,
        3 to R.drawable.ic_hoenn_diveball,
        4 to R.drawable.ic_sinnoh_duskball,
        5 to R.drawable.ic_unova_dreamball,
        6 to R.drawable.ic_kalos_healball,
        7 to R.drawable.ic_alola_beastball,
        8 to R.drawable.ic_galar_quickball,
        9 to R.drawable.ic_paldea_friendball,
    )

    private fun determineGeneration(id: Int): Int {
        return when (id) {
            in 1..151 -> 1 // Kanto
            in 152..251 -> 2 // Johto
            in 252..386 -> 3
            in 387..493 -> 4
            in 494..649 -> 5
            in 650..721 -> 6
            in 722..809 -> 7
            in 810..905 -> 8
            in 906..1025 -> 9
            else -> 0
        }
    }

    /**
     * GET POKEMONS BY GENERATION PAGINATED. CHECK IF DATA IS IN DB OR GET FROM API AND SAVE IT IN DB
     */
    suspend fun getPokemonsByGeneration(generation: Int, limit: Int, offset: Int): List<PokemonEntity> {

        val startId = generationMap[generation]?.firstPokemonId ?: 1
        val endId = generationMap[generation]?.lastPokemonId ?: 1025

        val adjustedLimit = if (startId + offset + limit - 1 > endId) {
            endId - (startId + offset) + 1
        } else {
            limit
        }
        if (adjustedLimit == 0){
            return emptyList()
        }

        val pokemonsInDb = pokemonDao.getPokemonByGeneration(generation, adjustedLimit, offset)

        if (pokemonsInDb.isNotEmpty() && pokemonsInDb.size == adjustedLimit) {
            return pokemonsInDb
        }

        val pokemons = fetchPokemonDetailsFromApi(startId + offset, startId + offset + adjustedLimit - 1)

        pokemonDao.insertPokemon(pokemons)

        return pokemons
    }

    /**
     * Preload from all generations: region, first and last pokemon id, and store it in
     * in [generationRegionMap]
     */
    suspend fun preloadGenerationData() {
        val pokeRegion = PokemonRegion(
            "All",
            1,
            1025,
            R.drawable.ic_default_pokeball
            )
        generationMap[0] = pokeRegion
        for (generationId in 1..9) {
            val generationResponse = apiService.getPokemonGeneration(generationId)
            if (generationResponse.isSuccessful) {
                val body = generationResponse.body()
                if (body != null) {
                    val sortedSpecies = body.pokemonSpecies.sortedBy { it.url.dropLast(1).split("/").last().toInt() }
                    val image = regionImageMap[generationId]
                    val pokeRegion = PokemonRegion(
                        region = body.name.split("-").last().uppercase(),
                        firstPokemonId = sortedSpecies.first().url
                            .dropLast(1)
                            .split("/")
                            .last()
                            .toInt(),
                        lastPokemonId = sortedSpecies.last().url
                            .dropLast(1)
                            .split("/")
                            .last()
                            .toInt(),
                        regionIcon = image ?: R.drawable.ic_default_pokeball
                    )
                    generationMap[generationId] = pokeRegion
                }
            }
        }
    }

    fun getRegionGenerationMap(): Map<Int, PokemonRegion> {
        return generationMap
    }

    /**
     * SAVE POKEMON DETAIL IN DB [PokemonEntity]
     */
    suspend fun fetchPokemonDetailsFromApi(startId: Int, endId: Int): List<PokemonEntity>{
        val pokemons = mutableListOf<PokemonEntity>()

        for (id in startId..endId){
            val response = apiService.getPokemonDetails(id)
            if (response.isSuccessful) {
                response.body()?.let { pokemon ->
                    val entity = PokemonEntity(
                        id = pokemon.id,
                        name = pokemon.name,
                        type1 = pokemon.types[0].type.name,
                        type2 = pokemon.types.getOrNull(1)?.type?.name,
                        spriteUrl = pokemon.sprites.other.officialArtwork.frontDefault,
                        generation = determineGeneration(id)
                    )
                    pokemons.add(entity)
                }
            }
        }
        pokemonDao.insertPokemon(pokemons)
        return pokemons
    }
}