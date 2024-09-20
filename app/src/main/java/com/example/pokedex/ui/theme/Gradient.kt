package com.example.pokedex.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val MetalRedGradient = Brush.linearGradient(
    colors = listOf(
        PokedexRed,
        Color(0xFFC62828)
    ),
    start = Offset(0f, 0f),
    end = Offset( 0f, 1000f)
)