package com.example.pokedex.ui.pokemon_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.Chain
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.repositories.PokeDetailRepository
import com.example.pokedex.room.entity.PokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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


    private suspend fun getPokemonDetail(id: Int){
        viewModelScope.launch {
            val poke = repository.getPokemonDetail(id)
            if (poke.isSuccessful && poke.body() != null){
                _pokemon.value = poke.body()
            }
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

    private suspend fun getPokemonEvolution(url: String){
        viewModelScope.launch {
            val evol = repository.getEvolutionChain(url)
            if (evol.isSuccessful && evol.body() != null){
                _evolutionChain.value = evol.body()?.chain
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