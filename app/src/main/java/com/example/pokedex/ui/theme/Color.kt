package com.example.pokedex.ui.theme

import androidx.compose.ui.graphics.Color

val PokedexRed = Color(0xFFD32F2F) // Rouge Pokedex, utilisé pour l'accent principal
val PokedexRedTransparant = Color(0x33D32F2F)
val PokedexRedDarkTransparant = Color(0x88D32F2F)
val WaterBlue = Color(0xFF1976D2)  // Bleu Eau, utilisé pour les fonctionnalités secondaires
val WaterBlueTransparent = Color(0x881976D2)  // Bleu Eau, utilisé pour les fonctionnalités secondaires
val GrassGreen = Color(0xFF388E3C) // Vert Herbe, pour les éléments de confirmation ou de succès

// Couleurs secondaires
val ElectricYellow = Color(0xFFFBC02D) // Jaune Électrique, pour les accents
val SteelGray = Color(0xFF757575)      // Gris Acier, pour les textes secondaires et les éléments de fond
val PureWhite = Color(0xFFFFFFFF)      // Blanc pur, pour les fonds et les contrastes

// Couleurs de fond
val LightBackground = Color(0xFFF5F5F5) // Fond clair, utilisé pour les contenus principaux
val DarkBackground = Color(0xFF212121)  // Fond sombre, pour les thèmes sombres ou les détails contrastés

// Couleurs supplémentaires pour le thème sombre
val DarkGray = Color(0xFF424242)       // Gris sombre pour les éléments de fond dans le thème sombre
val LightGray = Color(0xFFBDBDBD) // Gris clair pour les textes secondaires sur les fonds sombres
val MyBlack = Color(0XFF000000)

// Couleurs pour les stats
val HPColor = Color(0xFFFF5959)         // Rouge vif pour HP (Points de vie)
val AttackColor = Color(0xFFF5AC78)     // Orange pour Attack (Attaque)
val DefenseColor = Color(0xFF30A7D7)   // Bleu clair pour Defense (Défense)
val SpecialAttackColor = Color(0xFF9DB7F5)  // Bleu pour Special Attack (Attaque Spéciale)
val SpecialDefenseColor = Color(0xFFA7DB8D) // Vert pour Special Defense (Défense Spéciale)
val SpeedColor = Color(0xFFFAE078)      // Jaune pour Speed (Vitesse)
val TotalStatsColor = Color(0xFFA8A77A)


// Définition des couleurs pour chaque type de Pokémon avec 20% de transparence
val NormalType = Color(0x90A8A77A)
val FightingType = Color(0x90C22E28)
val FlyingType = Color(0x90A98FF3)
val PoisonType = Color(0x90A33EA1)
val GroundType = Color(0x90E2BF65)
val RockType = Color(0x90B6A136)
val BugType = Color(0x90A6B91A)
val GhostType = Color(0x90673597)
val SteelType = Color(0x9068A090)
val FireType = Color(0x90EE8130)
val WaterType = Color(0x906399FF)
val GrassType = Color(0x9078C850)
val ElectricType = Color(0x90F7D02C)
val PsychicType = Color(0x90F95587)
val IceType = Color(0x9090C8FF)
val DragonType = Color(0x906F35FC)
val DarkType = Color(0x90370531)
val FairyType = Color(0x90D685AD)
val StellarType = Color(0x90E6E6FA)
val UnknownType = Color(0x90A8A8A8)

// Fonction utilitaire pour obtenir la couleur de fond en fonction du type
fun getPokemonBackgroundColor(type: String): Color {
    return when (type.lowercase()) {
        "normal" -> NormalType
        "fighting" -> FightingType
        "flying" -> FlyingType
        "poison" -> PoisonType
        "ground" -> GroundType
        "rock" -> RockType
        "bug" -> BugType
        "ghost" -> GhostType
        "steel" -> SteelType
        "fire" -> FireType
        "water" -> WaterType
        "grass" -> GrassType
        "electric" -> ElectricType
        "psychic" -> PsychicType
        "ice" -> IceType
        "dragon" -> DragonType
        "dark" -> DarkType
        "fairy" -> FairyType
        "stellar" -> StellarType
        "unknown" -> UnknownType
        else -> Color.Transparent
    }
}

fun getStatsColor(stat: String): Color{
    return when(stat){
        "hp" -> HPColor
        "attack" -> AttackColor
        "defense" -> DefenseColor
        "special-attack" -> SpecialAttackColor
        "special-defense" -> SpecialDefenseColor
        "speed" -> SpeedColor
        else -> Color.Transparent
    }
}
