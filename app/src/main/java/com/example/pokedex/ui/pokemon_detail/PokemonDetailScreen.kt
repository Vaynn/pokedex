package com.example.pokedex.ui.pokemon_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedex.PokemonSharedViewModel
import com.example.pokedex.R
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.ui.AnimatedBackground
import com.example.pokedex.ui.theme.DarkGray
import com.example.pokedex.ui.theme.LightBackground
import com.example.pokedex.ui.theme.PokedexRedTransparant
import com.example.pokedex.ui.theme.getPokemonBackgroundColor

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    navController: NavController,
    onPokemonClick:(Int, Brush) -> Unit,
    modifier: Modifier = Modifier
){
    val pokemon by viewModel.pokemon.collectAsState()
    val pokemonSpecy by viewModel.pokemonSpecies.collectAsState()



   pokemon?.let { it ->
       pokemonSpecy?.let {  species ->
           val pokemonColor = getPokemonBackgroundColor(it.types[0].type.name)
           Box(modifier = modifier.fillMaxSize()) {
               AnimatedBackground(pokemonColor)
               // Partie supérieure avec la couleur du type
               Column(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(300.dp)
                       .background(pokemonColor)
               ) {
                   Column(
                       modifier = Modifier
                           .align(Alignment.Start)
                           .padding(16.dp)
                   ) {
                       Row(
                           modifier = Modifier.align(Alignment.CenterHorizontally)
                       ) {
                           Text(
                               text = "#${it.id.toString().padStart(4, '0')}",
                               fontSize = 28.sp,
                               fontWeight = FontWeight.Bold,
                               color = Color.White
                           )
                           Spacer(modifier = Modifier.width(8.dp))
                           Text(
                               text = it.name.uppercase().replace("-", " "),
                               fontSize = 28.sp,
                               fontWeight = FontWeight.Bold,
                               color = Color.White
                           )
                       }
                       Spacer(modifier = Modifier.height(8.dp))
                       Row(
                           modifier = Modifier.align(Alignment.CenterHorizontally)
                       ) {
                           for (type in it.types) {
                               Text(
                                   text = type.type.name.uppercase(),
                                   fontSize = 16.sp,
                                   fontWeight = FontWeight.Bold,
                                   color = Color.White,
                                   modifier = Modifier
                                       .padding(end = 8.dp)
                                       .background(
                                           getPokemonBackgroundColor(type.type.name).copy(
                                               alpha = 0.6f
                                           )
                                       )
                                       .padding(8.dp)
                               )
                           }
                       }

                   }
               }
               // Image du Pokémon chevauchant les deux sections
               AsyncImage(
                   model = it.sprites.other.officialArtwork.frontDefault,
                   contentDescription = it.name,
                   modifier = Modifier
                       .size(200.dp)
                       .align(Alignment.TopCenter)
                       .offset(y = 90.dp) // Chevauche la partie supérieure et la partie inférieure
                       .zIndex(1f),
                   contentScale = ContentScale.Crop
               )

               // Partie inférieure avec une Card blanche contenant les informations
               Card(
                   modifier = Modifier
                       .fillMaxWidth()
                       .fillMaxHeight()
                       .padding(top = 250.dp)
                       .zIndex(0.5f),
                   shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                   elevation = CardDefaults.cardElevation(8.dp),
                   colors = CardDefaults.cardColors(containerColor = LightBackground),
               ) {
                   Column(
                       modifier = Modifier
                           .fillMaxSize()
                           .padding(16.dp)
                   ) {
                       Spacer(modifier = Modifier.height(20.dp))

                       PokeTabRow(
                           pokemon = it,
                           species = species,
                           viewModel = viewModel,
                           onPokemonClick = onPokemonClick
                       )
                   }
               }
           }

       }

   }

}

@Composable
fun PokeTabRow(
    pokemon: Pokemon,
    species: PokemonSpecy,
    onPokemonClick: (Int, Brush) -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val titles = listOf(
        R.string.about,
        R.string.base_stats,
        R.string.evolution,
        R.string.moves
    )

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier.fillMaxWidth(),
        containerColor = LightBackground

    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index},
                text = { Text(
                    text = stringResource(id = title),
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTabIndex == index) DarkGray else Color.Gray
                    )
                },
                modifier = Modifier
                    .background(if (selectedTabIndex == index) PokedexRedTransparant else Color.Transparent)

            )
        }
    }
    Spacer(Modifier.height(12.dp))
    when (selectedTabIndex) {
        0 -> About(pokemon, species)
        1 -> BaseStats(pokemon)
        2 -> Evolution(species, viewModel, onPokemonClick = onPokemonClick)
        3 -> Moves()
    }
}


@Composable
fun Moves() {
    TODO("Not yet implemented")
}







