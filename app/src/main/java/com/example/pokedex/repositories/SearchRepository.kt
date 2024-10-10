package com.example.pokedex.repositories

import androidx.compose.runtime.mutableStateOf
import com.example.pokedex.data.PokemonShort
import com.example.pokedex.network.PokeApiService
import com.example.pokedex.room.dao.PokemonShortDao
import com.example.pokedex.room.entity.PokemonEntity
import com.example.pokedex.room.entity.PokemonShortEntity
import com.example.pokedex.ui.determineGeneration
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow


class SearchRepository @Inject constructor(
    private val pokemonShortDao: PokemonShortDao,
    private val api: PokeApiService
) {

    suspend fun searchPokemonDaoList(query: String): List<PokemonEntity> = coroutineScope {
        val pokeDaoList = pokemonShortDao.searchPokemonByName(query)

        val pokemonListDeferred = pokeDaoList.map { pokemon ->
            async {
                val detail = api.getPokemonDetailsByUrl(pokemon.url)
                if (detail.isSuccessful && detail.body() != null
                    && !detail.body()?.sprites?.other?.officialArtwork?.frontDefault.isNullOrBlank()
                    && detail.body()?.isDefault == true
                ) {
                    detail.body()?.let { poke ->
                        PokemonEntity(
                            id = poke.id,
                            name = poke.name,
                            type1 = poke.types[0].type.name,
                            type2 = poke.types.getOrNull(1)?.type?.name,
                            spriteUrl = poke.sprites.other.officialArtwork.frontDefault,
                            generation = determineGeneration(poke.id)
                        )
                    }
                } else {
                    null
                }
            }
        }

        val pokemonList =
            pokemonListDeferred.awaitAll().filterNotNull()

        return@coroutineScope pokemonList
    }
}