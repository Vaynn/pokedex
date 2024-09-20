package com.example.pokedex.ui.pokemon_detail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokedex.R
import com.example.pokedex.data.Icons
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.data.Species
import com.example.pokedex.ui.theme.DarkGray
import com.example.pokedex.ui.theme.LightBackground
import com.example.pokedex.ui.theme.LightGray
import com.example.pokedex.ui.theme.PokedexRed
import com.example.pokedex.ui.theme.PokedexRedDarkTransparant
import com.example.pokedex.ui.theme.PokedexRedTransparant
import com.example.pokedex.ui.theme.TotalStatsColor
import com.example.pokedex.ui.theme.getPokemonBackgroundColor
import com.example.pokedex.ui.theme.getStatsColor
import java.util.Locale
import kotlin.random.Random

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    val pokemon by viewModel.pokemon.collectAsState()
    val pokemonSpecy by viewModel.pokemonSpecies.collectAsState()

   pokemon?.let { it ->
       pokemonSpecy?.let {  species ->
           val pokemonColor = getPokemonBackgroundColor(it.types[0].type.name)
           Box(modifier = Modifier.fillMaxSize()) {
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
                               text = it.name.uppercase(),
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

                       PokeTabRow(pokemon = it, species = species)

                   }
               }
           }

       }

   }

}

@Composable
fun PokeTabRow(pokemon: Pokemon, species: PokemonSpecy, modifier: Modifier = Modifier){
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
        2 -> Evolution()
        3 -> Moves()
    }
}

@Composable
fun BaseStats(pokemon: Pokemon, modifier: Modifier = Modifier) {

    var total: Int = 0
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

        pokemon.stats.forEach { stat ->
            StatBar(
                statName = stat.stat.name,
                statValue = stat.baseStat,
                statMaxValue = 300 ,
                statColor = getStatsColor(stat.stat.name)
            )
            Spacer(modifier = Modifier.height(12.dp))
            total += stat.baseStat
        }
        StatBar(
            statName = stringResource(id = R.string.total),
            statValue = total,
            statMaxValue = 5000 ,
            statColor = TotalStatsColor
        )
    }

}

@Composable
fun Moves() {
    TODO("Not yet implemented")
}

@Composable
fun Evolution() {
    TODO("Not yet implemented")
}

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

@Composable
fun PokeToast(text: String, @DrawableRes iconsResource: Int, modifier: Modifier = Modifier){
    Surface(
        color = PokedexRedTransparant,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(50.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(id = iconsResource),
                contentDescription = "",
                tint = DarkGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                color = DarkGray,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@Composable
fun CenterTitleDivider(@StringRes title: Int){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(thickness = 4.dp, color = PokedexRed, modifier = Modifier.weight(1f))
        Text(text = stringResource(id = title), color = DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        HorizontalDivider(thickness = 4.dp, color = PokedexRed, modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatBar(statName: String, statValue: Int, statMaxValue: Int, statColor: Color, modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = statName.uppercase().replace("SPECIAL-", "SP. "),
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = modifier.width(8.dp))

        Text(
            text = statValue.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            modifier = Modifier.width(30.dp)
        )

        Spacer(modifier = modifier.width(4.dp))

        Box(
            modifier = Modifier
                .height(12.dp) // Hauteur fixe pour la barre horizontale
                .weight(1f)
                .background(Color.LightGray.copy(alpha = 0.3f)) // Couleur de fond
        ) {
            // Barre de progression en fonction de la valeur
            Box(
                modifier = Modifier
                    .fillMaxHeight() // Prendre toute la hauteur
                    .fillMaxWidth(
                        (statValue / statMaxValue.toFloat()).coerceIn(
                            0f,
                            1f
                        )
                    ) // Ajuster la largeur en fonction de la progression
                    .background(statColor) // Couleur de la barre de progression
            )
        }
    }
}

@Composable
fun AnimatedBackground(bubbleColor: Color) {
    val ballCount = 10
    val balls = remember { generateBalls(ballCount) }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    balls.forEach { ball ->
        val animatedX = infiniteTransition.animateFloat(
            initialValue = ball.positionX - 100f,
            targetValue = ball.positionX + 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(ball.speed, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        val animatedY = infiniteTransition.animateFloat(
            initialValue = ball.positionY - 50f,
            targetValue = ball.positionY + 50f,
            animationSpec = infiniteRepeatable(
                animation = tween(ball.speed, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = bubbleColor.copy(alpha = 0.3f),
                radius = ball.radius * 2f,
                center = Offset(animatedX.value, animatedY.value)
            )


            drawCircle(
                color = bubbleColor.copy(alpha = 1f),
                radius = ball.radius,
                center = Offset(animatedX.value, animatedY.value)
            )
        }
    }
}

data class Ball(
    val positionX: Float,
    val positionY: Float,
    val radius: Float,
    val color: Color,
    val speed: Int
)

fun generateBalls(count: Int): List<Ball> {
    val random = Random(System.currentTimeMillis())
    return List(count) {
        Ball(
            positionX = random.nextFloat() * 1000f, // Largeur de l'écran
            positionY = random.nextFloat() * 1000f, // Hauteur de l'écran
            radius = random.nextFloat() * 30f + 10f, // Taille des boules
            color = Color(
                random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                1f
            ), // Couleur aléatoire
            speed = random.nextInt(2000, 5000) // Vitesse aléatoire entre 2 et 5 secondes
        )
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PokemonDetailScreenPreview(){
    PokemonDetailScreen(pokemonId = 1)
}