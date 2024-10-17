package com.example.pokedex.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.PokemonSharedViewModel
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
    private val repository: PokemonRepository,

): ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val pokemonList: StateFlow<List<PokemonEntity>> = _pokemonList

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _generationRegionMap = MutableStateFlow<Map<Int, PokemonRegion>>(emptyMap())
    val generationRegionMap: StateFlow<Map<Int, PokemonRegion>> = _generationRegionMap

    private var currentGeneration = 0
    private var currentPage = 0
    private val pageSize = 20
    private val _hasReachedEnd = MutableStateFlow<Boolean>(false)
    val hasReachedEnd: StateFlow<Boolean> = _hasReachedEnd

    private val _selectedGeneration = MutableStateFlow(0)
    val selectedGeneration: StateFlow<Int> = _selectedGeneration

    private val _isRegionFilterReady = MutableStateFlow(false)
    val isRegionFilterReady: StateFlow<Boolean> = _isRegionFilterReady

    init {
        viewModelScope.launch {
            when (val result = repository.preloadGenerationData()) {
                is ApiResponse.Success -> {
                    _generationRegionMap.value = repository.getRegionGenerationMap()
                    loadPokemonsForGeneration(currentGeneration)
                }
                is ApiResponse.Error -> {
                    _errorMessage.value = result.message
                }
            }
        }
    }

    fun loadPokemonsForGeneration(generation: Int) {
        _selectedGeneration.value = generation
        currentGeneration = generation
        currentPage = 0
        _pokemonList.value = emptyList()
        _hasReachedEnd.value = false
        _loading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            when (val pokemons = repository.getPokemonsByGeneration(currentGeneration, pageSize, currentPage * pageSize)){
                is ApiResponse.Success -> {
                    _pokemonList.value = pokemons.data
                    currentPage++
                    _loading.value = false
                    _isRegionFilterReady.value = true
                }
                is ApiResponse.Error -> {
                    _errorMessage.value = pokemons.message
                }
            }
            _loading.value = false
        }
    }
    fun loadNextPage() {
        if (!_loading.value && !_hasReachedEnd.value) {
            _errorMessage.value = null
            viewModelScope.launch {
                _loading.value = true
                when (val pokemons = repository.getPokemonsByGeneration(
                    currentGeneration,
                    pageSize,
                    currentPage * pageSize)){
                    is ApiResponse.Success -> {
                        if (pokemons.data.isNotEmpty()) {
                            _pokemonList.value += pokemons.data
                            _loading.value = false
                            currentPage++
                        } else {
                            _hasReachedEnd.value = true
                        }
                        _loading.value = false
                    }
                    is ApiResponse.Error -> {
                        _errorMessage.value = pokemons.message
                        _loading.value = false
                    }
                }

            }
        }
    }

    /**
     * add X times 0 to make 4 digit number
     */
    fun addZerosToPokemonOrder(order: Int): String{
        return "#${order.toString().padStart(4, '0')}"
    }
}