package com.example.pokedex.ui.pokemon_detail

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex.data.Chain
import com.example.pokedex.data.Evolution
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.data.Species
import com.example.pokedex.data.Variety
import com.example.pokedex.repositories.PokeDetailRepository
import com.example.pokedex.room.entity.PokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokeDetailRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _pokemon: MutableStateFlow<Pokemon?> = MutableStateFlow(null)
    val pokemon: StateFlow<Pokemon?> = _pokemon

    private val _pokemonSpecies: MutableStateFlow<PokemonSpecy?> = MutableStateFlow(null)
    val pokemonSpecies: StateFlow<PokemonSpecy?> = _pokemonSpecies

    private val _evolutionChain: MutableStateFlow<Chain?> = MutableStateFlow(null)
    val evolutionChain: StateFlow<Chain?> = _evolutionChain

    private val _regionalEvolutionChain = MutableStateFlow<MutableMap<String, MutableList<Evol>>>(
        mutableMapOf()
    )
    val regionalEvolutionChain: StateFlow<Map<String, MutableList<Evol>>> = _regionalEvolutionChain


    private suspend fun getPokemonDetail(id: Int){
        viewModelScope.launch {
            val poke = repository.getPokemonDetail(id)
            if (poke.isSuccessful && poke.body() != null){
                _pokemon.value = poke.body()
            }
        }
    }

    suspend fun getPokemonByName(name: String): PokemonEntity?{
        return withContext(Dispatchers.IO){
            var poke = repository.getPokemonByNameDao(name)
            if (poke == null){
                val pokeApi = repository.getPokemonDetailByNameApi(name)
                if (pokeApi.isSuccessful && pokeApi.body() != null){
                    val resp = pokeApi.body()
                    resp?.let {
                        val pokemonEntity = PokemonEntity(
                            name = it.name,
                            id = it.id,
                            generation = 0,
                            spriteUrl = it.sprites.other.officialArtwork.frontDefault,
                            type1 = it.types[0].type.name,
                            type2 = if (it.types.size > 1) it.types[1].type.name else ""
                        )
                        poke = pokemonEntity
                    }

                }

            }
            poke
        }
    }

    suspend fun getPokemonByUrl(url: String): PokemonEntity? {
        return withContext(Dispatchers.IO) {
            val pokeApi = repository.getPokemonDetailByUrl(url)
            if (pokeApi.isSuccessful && pokeApi.body() != null) {
                val resp = pokeApi.body()
                resp?.let {
                    return@withContext PokemonEntity(
                        name = it.name,
                        id = it.id,
                        generation = 0,
                        spriteUrl = it.sprites.other.officialArtwork.frontDefault,
                        type1 = it.types[0].type.name,
                        type2 = if (it.types.size > 1) it.types[1].type.name else ""
                    )
                }
            }
            null
        }
    }

    private suspend fun getPokemonSpecies(id: Int){
        viewModelScope.launch {
            val specy = repository.getPokemonSpecies(id)
            if (specy.isSuccessful && specy.body() != null){
                _pokemonSpecies.value = specy.body()
            }
        }
    }

    /**
     * Add in a map the special evolution of a pokemon
     */
    suspend fun addPokemonSpeciesEvolution(name: String) {
        viewModelScope.launch {
            val species = repository.getPokemonSpeciesByName(name)
            if (species.isSuccessful && species.body() != null) {
                val varieties = species.body()?.varieties
                val defaultPokemon = varieties?.find { it.isDefault }
                val defaultPokemonName = defaultPokemon?.pokemon?.name?.split("-")?.firstOrNull() ?: ""
                varieties?.forEach { variety ->
                    if (!variety.isDefault) {
                        val varietyName = variety
                            .pokemon
                            .name
                            .replace("${defaultPokemonName}-", "")
                        val updatedMap = _regionalEvolutionChain.value.toMutableMap()
                        val list = updatedMap.getOrPut(varietyName) { mutableListOf() }
                        val newEvol = Evol(name = variety.pokemon.name, url = variety.pokemon.url)
                        if (!list.contains(newEvol)) {
                            list.add(newEvol)
                            updatedMap[varietyName] = list
                            _regionalEvolutionChain.value = updatedMap
                        }
                    }
                }
            }
        }
    }

    suspend fun getPokemonEvolution(url: String){
        viewModelScope.launch {
            val evol = repository.getEvolutionChain(url)
            if (evol.isSuccessful && evol.body() != null){
                _evolutionChain.value = evol.body()?.chain
            }
        }
    }

    /**
     * get Regionals Form of a Pokemon and add it in a Map
     */
    fun getRegionalsEvolution() {
        _pokemonSpecies.value?.varieties.let { varieties ->
            val varietyPrefixName =
                _pokemon.value?.name?.split("-")?.firstOrNull() ?: ""
            println("VARIETY PREFIX NAME $varietyPrefixName")
            varieties?.forEach { variety ->
                if(!variety.isDefault){
                    val varietyName = variety
                        .pokemon
                        .name
                        .replace("${varietyPrefixName}-" ?: "", "")
                    println("VARIETY NAME ${variety.pokemon.name}")
                    val updatedMap = _regionalEvolutionChain.value.toMutableMap()
                    val list = updatedMap.getOrPut(varietyName) { mutableListOf() }
                    val newEvol = Evol(name = variety.pokemon.name, url = variety.pokemon.url)
                    if (!list.contains(newEvol)) {
                        list.add(newEvol)
                        updatedMap[varietyName] = list
                        _regionalEvolutionChain.value = updatedMap
                    }
                }

            }
        }
    }


    init {
        viewModelScope.launch {
            val pokemonId = savedStateHandle.get<Int>("pokemonId") ?: -1
            if (pokemonId >= 0){
                getPokemonDetail(pokemonId)
                getPokemonSpecies(pokemonId)
            }
        }
    }
}