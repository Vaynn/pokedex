package com.example.pokedex.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pokedex.R

// Set of Material typography styles to start with
val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val Montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

// Définir la typographie de l'application
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        color = PokedexRed // Utilisé pour les titres principaux
    ),
    headlineMedium = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        color = PokedexRed // Pour les sous-titres
    ),
    titleLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        color = SteelGray // Pour les titres de section
    ),
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = SteelGray // Texte principal
    ),
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = SteelGray // Texte de taille moyenne
    ),
    labelLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = PureWhite // Texte sur les boutons
    ),
    labelSmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        color = LightGray // Pour les légendes et petits textes
    )
)