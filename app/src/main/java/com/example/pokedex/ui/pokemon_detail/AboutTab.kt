package com.example.pokedex.ui.pokemon_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedex.R
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.ui.theme.DarkGray
import java.util.Locale

@Composable
fun About(pokemon: Pokemon, species: PokemonSpecy, modifier: Modifier = Modifier) {
    val genus = species.genera.find { it -> it.language.name == "en" }
    val descripion = species.flavorTextEntries.find { it -> it.language.name == "en" }
    val pokeWeight: String  =
        String.format(Locale.US, "%.1f",(pokemon.weight/ 10f)) + " kg"
    val pokeHeight: String  =
        String.format(Locale.US, "%.1f",(pokemon.height/ 10f)) + " m"
    Row(modifier = modifier.fillMaxWidth()) {
        PokeToast(text = pokeWeight, iconsResource = R.drawable.ic_weight, modifier = Modifier.weight(1f))

        Spacer(Modifier.width(8.dp))

        PokeToast(text = pokeHeight, iconsResource = R.drawable.ic_height, modifier = Modifier.weight(1f))
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = genus?.genus?.uppercase() ?: "",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = DarkGray
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = descripion?.flavorText?.replace("\n", " ") ?: "",
        fontSize = 18.sp,
        color = DarkGray
    )

    Spacer(modifier = Modifier.height(10.dp))

    CenterTitleDivider(title = R.string.shiny_form)
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = pokemon.sprites.other.officialArtwork.frontShiny,
            contentDescription = pokemon.name,
            modifier = Modifier
                .size(100.dp)
            ,
            contentScale = ContentScale.Crop
        )
    }


}