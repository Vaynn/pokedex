package com.example.pokedex


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.pokedex.ui.theme.MetalRedGradient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonSharedViewModel @Inject constructor() : ViewModel(){

    val selectedPokemonColor: MutableState<Brush> = mutableStateOf(MetalRedGradient)

    fun updatePokemonColor(brush: Brush) {
        selectedPokemonColor.value = brush
    }
}