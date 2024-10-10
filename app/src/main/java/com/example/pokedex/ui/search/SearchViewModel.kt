package com.example.pokedex.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.repositories.PokemonRepository
import com.example.pokedex.repositories.SearchRepository
import com.example.pokedex.room.entity.PokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
): ViewModel() {

    private val _searchResults = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val searchResult: StateFlow<List<PokemonEntity>> = _searchResults

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .filter { query -> query.isNotBlank() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    _isLoading.value = true
                    val results = repository.searchPokemonDaoList(query)
                    _searchResults.value = results
                    _isLoading.value = false
                }
        }
    }

    fun onQueryChanged(query: String){
        _searchQuery.value = query
        if (query.isBlank()){
            _searchResults.value = emptyList()
            _isLoading.value = false
        } else {
            _isLoading.value = true
        }
    }

    /**
     * add X times 0 to make 4 digit number
     */
    fun addZerosToPokemonOrder(order: Int): String{
        return "#${order.toString().padStart(4, '0')}"
    }
}