package com.example.pokedex.ui.pokemon_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R

import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.room.entity.PokemonEntity
import com.example.pokedex.ui.CenterTitleDivider
import com.example.pokedex.ui.CenterTitleDividerMini
import com.example.pokedex.ui.LoadingPokeball
import com.example.pokedex.ui.theme.DarkGray
import com.example.pokedex.ui.theme.LightBackground
import com.example.pokedex.ui.theme.PokedexRed
import com.example.pokedex.ui.theme.PokedexRedTransparant
import com.example.pokedex.ui.theme.WaterBlue
import com.example.pokedex.ui.theme.WaterBlueTransparent
import com.example.pokedex.ui.theme.getPokemonBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun Evolution(
    species: PokemonSpecy,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    onPokemonClick: (Int, Brush) -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val pokemon by viewModel.pokemon.collectAsState()
    val evolutionChain by viewModel.evolutionChain.collectAsState()
    val regionalEvolutionChain by viewModel.regionalEvolutionChain.collectAsState()

    LaunchedEffect(key1 = species.evolutionChain.url) {
        viewModel.loadEvolutionData(species.evolutionChain.url)
    }

    if (!isLoading) {

        val hasClassicEvolution = evolutionChain?.evolvesTo?.isNotEmpty() ?: false
        val hasSpecialEvolution = regionalEvolutionChain.isNotEmpty()

        if (!hasClassicEvolution && !hasSpecialEvolution) {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pokemon?.sprites?.other?.officialArtwork?.frontDefault)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Grayed Image",
                    modifier = Modifier.size(150.dp),
                    colorFilter = ColorFilter.tint(Color.Gray, BlendMode.SrcIn),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WaterBlueTransparent)
                        .border(
                            width = 2.dp,
                            color = WaterBlue,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_evolution).uppercase(),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp,
                        color = LightBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

            }

        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
            ) {
                item {
                    ClassicEvolution(
                        species = species,
                        viewModel = viewModel,
                        onPokemonClick = onPokemonClick,
                        modifier = modifier
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    SpecialEvolution(
                        species = species,
                        onPokemonClick = onPokemonClick,
                        modifier = modifier
                    )
                }
            }
        }
    } else {
        LoadingPokeball()
    }


}

data class Evol(
    val name: String,
    val url: String
)

@Composable
fun ClassicEvolution(
    species: PokemonSpecy,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    onPokemonClick: (Int, Brush) -> Unit,
    modifier: Modifier = Modifier
){
    val evolution by viewModel.evolutionChain.collectAsState()
    val detailMap by viewModel.classicEvolutionDetailMap.collectAsState()

    evolution?.let { evol ->
    val allBranches = viewModel.getAllBranches(evol)
    val goodBranch = viewModel.findBranchContainingPokemon(allBranches, species.name)
    if (goodBranch[0].size > 1) {
        Column(modifier = modifier) {
            CenterTitleDivider(title = R.string.evolution)
            Spacer(modifier = Modifier.height(12.dp))
        }
        goodBranch.forEach { branch ->
                DisplayEvolutionChain(
                    chain = branch,
                    detailMap = detailMap,
                    species = species,
                    onPokemonClick = onPokemonClick,
                    isClickable = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SpecialEvolution(
    species: PokemonSpecy,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    onPokemonClick: (Int, Brush) -> Unit,
    isClickable: Boolean = false,
    modifier: Modifier = Modifier
) {
    val regionalEvolutionMap by viewModel.regionalDetailMap.collectAsState()
    val regionalEvolution by viewModel.regionalEvolutionChain.collectAsState()


        for ((region, listPokemon) in regionalEvolution) {
            val isContainPokemonName = listPokemon.any { it ->
                it.name.contains(species.name, ignoreCase = true)
            }
            if (isContainPokemonName) {
                Column(modifier = modifier) {
                    CenterTitleDividerMini(title = "${region.replace("-", " ").uppercase()} FORM")
                    Spacer(modifier = Modifier.height(12.dp))
                }

                DisplayEvolutionChain(
                    chain = listPokemon,
                    detailMap = regionalEvolutionMap,
                    species = species,
                    onPokemonClick = onPokemonClick,
                    isClickable = false
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }



@Composable
fun DisplayEvolutionChain(
    chain: List<Evol>,
    detailMap: Map<String, PokemonEntity>,
    species: PokemonSpecy,
    onPokemonClick: (Int, Brush) -> Unit,
    isClickable: Boolean,
    modifier: Modifier = Modifier
){
    println("Detail map content: $detailMap")
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        chain.forEachIndexed { index, ev ->
            val detail = detailMap[ev.name]
            println("Displaying evolution: ${ev.name}, detail: $detail")
            if (detail != null) {
                val pokemonColorType = detail?.type1 ?: "unknown"
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (ev.name != species.name && isClickable) {
                                val pokemonId =
                                    detailMap[ev.name]?.id ?: return@clickable
                                onPokemonClick(
                                    pokemonId,
                                    SolidColor(getPokemonBackgroundColor(pokemonColorType))
                                )
                            }
                        }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(detail?.spriteUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = ev.name,
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = ev.name.uppercase().replace("-", " "),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkGray
                    )
                }
                if (index < chain.size - 1) {
                    Icon(
                        painterResource(id = R.drawable.ic_evolution_arrow),
                        contentDescription = stringResource(id = R.string.arrow),
                        tint = PokedexRed,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

    }
}


