package com.example.pokedex.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.R
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonShort
import com.example.pokedex.models.PokemonGridItem
import com.example.pokedex.models.PokemonRegion
import com.example.pokedex.network.ApiResponse
import com.example.pokedex.repositories.PokemonRepository
import com.example.pokedex.room.entity.PokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.IntStream.range
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val pokemonList: StateFlow<List<PokemonEntity>> = _pokemonList

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _generationRegionMap = MutableStateFlow<Map<Int, PokemonRegion>>(emptyMap())
    val generationRegionMap: StateFlow<Map<Int, PokemonRegion>> = _generationRegionMap

    private var currentGeneration = 0
    private var currentPage = 0
    private val pageSize = 20
    private val _hasReachedEnd = MutableStateFlow<Boolean>(false)
    val hasReachedEnd: StateFlow<Boolean> = _hasReachedEnd

    private val _selectedGeneration = MutableStateFlow(0)
    val selectedGeneration: StateFlow<Int> = _selectedGeneration

    init {
        viewModelScope.launch {
            repository.preloadGenerationData()
            _generationRegionMap.value = repository.getRegionGenerationMap()
            loadPokemonsForGeneration(currentGeneration)
        }
    }

    fun loadPokemonsForGeneration(generation: Int) {
        _selectedGeneration.value = generation
        currentGeneration = generation
        currentPage = 0
        _pokemonList.value = emptyList()
        _hasReachedEnd.value = false
        _loading.value = true
        viewModelScope.launch {
            val pokemons = repository.getPokemonsByGeneration(currentGeneration, pageSize, currentPage * pageSize)
            _pokemonList.value = pokemons
            currentPage++
            _loading.value = false
        }
    }
    fun loadNextPage() {
        if (!_loading.value && !_hasReachedEnd.value) {

            viewModelScope.launch {
                _loading.value = true
                val pokemons = repository.getPokemonsByGeneration(
                    currentGeneration,
                    pageSize,
                    currentPage * pageSize)
                if (pokemons.isNotEmpty()) {
                    _pokemonList.value += pokemons
                    _loading.value = false
                    currentPage++
                } else {
                    _hasReachedEnd.value = true
                }
                _loading.value = false
            }
        }
    }

    /**
     * add X times 0 to make 4 digit number
     */
    fun addZerosToPokemonOrder(order: Int): String{
        return "#" + "0".repeat(4 - order.toString().length) + order.toString()
    }
}