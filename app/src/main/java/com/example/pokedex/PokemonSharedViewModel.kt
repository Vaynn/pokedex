package com.example.pokedex


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.pokedex.ui.theme.MetalRedGradient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonSharedViewModel @Inject constructor() : ViewModel(){

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    val selectedPokemonColor: MutableState<Brush> = mutableStateOf(MetalRedGradient)

    fun updatePokemonColor(brush: Brush) {
        selectedPokemonColor.value = brush
    }

    fun setError(message: String?){
        _errorMessage.value = message
    }
     fun clearError() {
         _errorMessage.value = null
     }

}