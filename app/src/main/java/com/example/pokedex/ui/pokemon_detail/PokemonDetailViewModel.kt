package com.example.pokedex.ui.pokemon_detail

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex.data.Chain
import com.example.pokedex.data.Evolution
import com.example.pokedex.data.EvolvesTo
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.data.Species
import com.example.pokedex.data.Variety
import com.example.pokedex.repositories.PokeDetailRepository
import com.example.pokedex.room.entity.PokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    private val _classicEvolutionDetailMap: MutableStateFlow<MutableMap<String, PokemonEntity>> =
        MutableStateFlow(mutableMapOf())
    val classicEvolutionDetailMap: StateFlow<Map<String, PokemonEntity>> = _classicEvolutionDetailMap

    private val _regionalDetailMap: MutableStateFlow<MutableMap<String, PokemonEntity>> =
        MutableStateFlow(mutableMapOf())
    val regionalDetailMap: StateFlow<Map<String, PokemonEntity>> = _regionalDetailMap

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val pokemonId = savedStateHandle.get<Int>("pokemonId") ?: -1
            if (pokemonId >= 0){
                getPokemonDetail(pokemonId)
                getPokemonSpecies(pokemonId)

            }
        }
    }

    fun setIsLoading(isLoading: Boolean){
        _isLoading.value = isLoading
    }

    private suspend fun getPokemonDetail(id: Int){
        viewModelScope.launch {
            val poke = repository.getPokemonDetail(id)
            if (poke.isSuccessful && poke.body() != null){
                _pokemon.value = poke.body()
            }
        }
    }

    fun addPokemonToRegionalDetailMap(name: String, entity: PokemonEntity) {
        _regionalDetailMap.value = _regionalDetailMap.value.toMutableMap().apply {
            if (!this.containsKey(name)){
                this[name] = entity
            }
        }
    }


    suspend fun getPokemonByName(name: String): PokemonEntity? = withContext(Dispatchers.IO) {
        var poke = repository.getPokemonByNameDao(name)
        if (poke == null) {
            val pokeApi = repository.getPokemonDetailByNameApi(name)
            if (pokeApi.isSuccessful && pokeApi.body() != null) {
                val resp = pokeApi.body()
                resp?.let {
                    poke = PokemonEntity(
                        name = it.name,
                        id = it.id,
                        generation = 0,
                        spriteUrl = it.sprites.other.officialArtwork.frontDefault,
                        type1 = it.types[0].type.name,
                        type2 = if (it.types.size > 1) it.types[1].type.name else ""
                    )
                }
            }
        }
        poke
    }

    suspend fun getPokemonByUrl(url: String): PokemonEntity? = withContext(Dispatchers.IO) {
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
    suspend fun addPokemonSpeciesEvolution(name: String) = withContext(Dispatchers.IO) {
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

    suspend fun getPokemonEvolution(url: String) = withContext(Dispatchers.IO) {
        val evol = repository.getEvolutionChain(url)
        if (evol.isSuccessful && evol.body() != null) {
            println("Evolution chain loaded: ${evol.body()?.chain}")
            _evolutionChain.value = evol.body()?.chain
        } else {
            println("Failed to load evolution chain")
        }
    }

    /**
     * get Regionals Form of a Pokemon and add it in a Map
     */
    fun getRegionalsEvolution() {
        _pokemonSpecies.value?.varieties.let { varieties ->
            val varietyPrefixName =
                _pokemon.value?.name?.split("-")?.firstOrNull() ?: ""
            varieties?.forEach { variety ->
                if(!variety.isDefault){
                    val varietyName = variety
                        .pokemon
                        .name
                        .replace("${varietyPrefixName}-" ?: "", "")

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

    suspend fun loadEvolutionData(pokemonEvolutionUrl: String){
        _isLoading.value = true
        try {
            getPokemonEvolution(pokemonEvolutionUrl)
            _evolutionChain.value?.let {
                val allBranches = getAllBranches(it)
                val goodBranch = findBranchContainingPokemon(allBranches, _pokemonSpecies.value?.name ?: "")
                println("Good branch found: $goodBranch")
                goodBranch.flatten().forEach { ev ->
                    if (!_classicEvolutionDetailMap.value.containsKey(ev.name)) {
                        getPokemonByName(ev.name)?.let { pokemon ->
                            _classicEvolutionDetailMap.value = _classicEvolutionDetailMap.value.toMutableMap().apply {
                                put(ev.name, pokemon)
                            }
                            println("Added to classicEvolutionDetailMap: ${pokemon.name}")
                            addPokemonSpeciesEvolution(pokemon.name)
                        }
                    }
                }
            }
            getRegionalsEvolution()
            if (_regionalEvolutionChain.value.isNotEmpty()) {
                for ((region, listPokemon) in _regionalEvolutionChain.value) {
                    val isContainPokemonName = listPokemon.any { it ->
                        it.name.contains(pokemonSpecies.value?.name ?: "", ignoreCase = true)
                    }
                    if (isContainPokemonName) {
                        listPokemon.forEach {
                                val pokemon = getPokemonByUrl(it.url)
                                if (pokemon != null) {
                                    addPokemonToRegionalDetailMap(pokemon.name, pokemon)
                                }
                            }
                        }
                    }
                }
            _isLoading.value = false
        } catch (e: Exception){
            println("error")
        }
    }

    fun getAllBranches(chain: Chain): List<List<Evol>> {
        val branches = mutableListOf<List<Evol>>()
        collectBranches(chain, mutableListOf(), branches)
        return branches
    }

    fun collectBranches(currentChain: Chain, currentBranch: MutableList<Evol>, branches: MutableList<List<Evol>>) {
        currentBranch.add(Evol(currentChain.species.name, currentChain.species.url))

        if (currentChain.evolvesTo.isEmpty()) {
            // Si aucune évolution suivante, ajouter la branche actuelle à la liste
            branches.add(currentBranch.toList())
        } else {
            for (nextEvolution in currentChain.evolvesTo) {
                // Cloner la branche actuelle et continuer à explorer
                collectBranches(nextEvolution, currentBranch.toMutableList(), branches)
            }
        }
    }

    fun collectBranches(currentChain: EvolvesTo, currentBranch: MutableList<Evol>, branches: MutableList<List<Evol>>) {
        currentBranch.add(Evol(currentChain.species.name, currentChain.species.url))

        if (currentChain.evolvesTo.isEmpty()) {
            // Si aucune évolution suivante, ajouter la branche actuelle à la liste
            branches.add(currentBranch.toList())
        } else {
            for (nextEvolution in currentChain.evolvesTo) {
                // Cloner la branche actuelle et continuer à explorer
                collectBranches(nextEvolution, currentBranch.toMutableList(), branches)
            }
        }
    }

    fun findBranchContainingPokemon(branches: List<List<Evol>>, currentPokemon: String): List<List<Evol>> {
        return branches.filter { branch ->
            branch.any { it.name == currentPokemon }
        }
    }

}