package com.example.pokedex.ui.pokemon_detail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokedex.R
import com.example.pokedex.data.Chain
import com.example.pokedex.data.EvolvesTo
import com.example.pokedex.data.PokemonSpecy
import com.example.pokedex.room.entity.PokemonEntity
import com.example.pokedex.ui.theme.DarkGray
import com.example.pokedex.ui.theme.PokedexRed
import com.example.pokedex.ui.theme.getPokemonBackgroundColor

@Composable
fun Evolution(
    species: PokemonSpecy,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    onPokemonClick: (Int, Brush) -> Unit,
    modifier: Modifier = Modifier
) {
    viewModel.getRegionalsEvolution()
    LazyColumn(
    modifier = modifier.fillMaxSize()
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
            SpecialEvolution(species = species, onPokemonClick = onPokemonClick, modifier = modifier)
        }

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
    val detailMap = remember { mutableStateMapOf<String, PokemonEntity>() }

    LaunchedEffect(key1 = species.evolutionChain.url) {
        viewModel.getPokemonEvolution(species.evolutionChain.url)

    }

    LaunchedEffect(key1 = evolution) {
        evolution?.let {
            val allBranches = getAllBranches(it)
            val goodBranch = findBranchContainingPokemon(allBranches, species.name)
            goodBranch.flatten().forEach { ev ->
                if (!detailMap.containsKey(ev.name)) {
                    viewModel.getPokemonByName(ev.name)?.let { pokemon ->
                        detailMap[ev.name] = pokemon
                        viewModel.addPokemonSpeciesEvolution(pokemon.name)
                    }
                }
            }
        }
    }

    evolution?.let { evol ->
    val allBranches = getAllBranches(evol)
    val goodBranch = findBranchContainingPokemon(allBranches, species.name)
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
){
    val regionalEvolutionMap = remember { mutableStateMapOf<String, PokemonEntity>() }
    val regionalEvolution by viewModel.regionalEvolutionChain.collectAsState()

    if(regionalEvolution.isNotEmpty()){
        for ((region, listPokemon) in regionalEvolution){
            val isContainPokemonName = listPokemon.any { it ->
                it.name.contains(species.name, ignoreCase = true)
            }
            if (isContainPokemonName){
                listPokemon.forEach {
                    LaunchedEffect(it.url) {
                        val pokemon = viewModel.getPokemonByUrl(it.url)
                        if (pokemon != null){
                            regionalEvolutionMap[pokemon.name] = pokemon
                        }
                    }
                }
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

}

@Composable
fun DisplayEvolutionChain(
    chain: List<Evol>,
    detailMap: MutableMap<String, PokemonEntity>,
    species: PokemonSpecy,
    onPokemonClick: (Int, Brush) -> Unit,
    isClickable: Boolean,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        chain.forEachIndexed { index, ev ->
            val detail = detailMap[ev.name]
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
                    model = detail?.spriteUrl,
                    contentDescription = ev.name,
                    modifier = Modifier
                        .size(80.dp),
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

fun getAllBranches(chain: Chain): List<List<Evol>> {
    val branches = mutableListOf<List<Evol>>()
    collectBranches(chain, mutableListOf(), branches)
    return branches
}

fun collectBranches(currentChain: Chain, currentBranch: MutableList<Evol>, branches: MutableList<List<Evol>>) {
    currentBranch.add(Evol(currentChain.species.name, currentChain.species.url))

    if (currentChain.evolvesTo.isEmpty()) {
        // Si aucune évolution suivante, ajouter la branche actuelle à la liste
        branches.add(currentBranch.toList())
    } else {
        for (nextEvolution in currentChain.evolvesTo) {
            // Cloner la branche actuelle et continuer à explorer
            collectBranches(nextEvolution, currentBranch.toMutableList(), branches)
        }
    }
}

fun collectBranches(currentChain: EvolvesTo, currentBranch: MutableList<Evol>, branches: MutableList<List<Evol>>) {
    currentBranch.add(Evol(currentChain.species.name, currentChain.species.url))

    if (currentChain.evolvesTo.isEmpty()) {
        // Si aucune évolution suivante, ajouter la branche actuelle à la liste
        branches.add(currentBranch.toList())
    } else {
        for (nextEvolution in currentChain.evolvesTo) {
            // Cloner la branche actuelle et continuer à explorer
            collectBranches(nextEvolution, currentBranch.toMutableList(), branches)
        }
    }
}

fun findBranchContainingPokemon(branches: List<List<Evol>>, currentPokemon: String): List<List<Evol>> {
    return branches.filter { branch ->
        branch.any { it.name == currentPokemon }
    }
}
