package com.example.pokedex.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.pokedex.PokemonSharedViewModel
import com.example.pokedex.R
import com.example.pokedex.data.Pokemon
import com.example.pokedex.models.PokemonGridItem
import com.example.pokedex.models.PokemonPreview
import com.example.pokedex.models.PokemonRegion
import com.example.pokedex.room.entity.PokemonEntity
import com.example.pokedex.ui.theme.ElectricYellow
import com.example.pokedex.ui.theme.PokedexRed
import com.example.pokedex.ui.theme.PokedexRedDarkTransparant
import com.example.pokedex.ui.theme.PokedexRedTransparant
import com.example.pokedex.ui.theme.PureWhite


import com.example.pokedex.ui.theme.getPokemonBackgroundColor

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPokemonClick: (Int, Brush) -> Unit,
    modifier: Modifier = Modifier
){

    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val regionByGeneration by viewModel.generationRegionMap.collectAsState()
    val selectedGeneration by viewModel.selectedGeneration.collectAsState()
    val gridState = rememberLazyGridState()
    var targetIndex by remember {
        mutableStateOf<Int? >(null)
    }
    val hasReachedEnd by viewModel.hasReachedEnd.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        RegionFilterRow(
            regionsMap = regionByGeneration,
            selectedGeneration = selectedGeneration,
            setGeneration = { generation ->
                viewModel.loadPokemonsForGeneration(generation)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            modifier = Modifier.
            fillMaxWidth(),
            thickness = 5.dp,
            color = PokedexRedTransparant
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = gridState
        ) {
            items(
                pokemonList,
                key = { it.id }
            ) { pokemon ->
                PokeCard(
                    pokemon = pokemon,
                    addZeroToPokemon = viewModel::addZerosToPokemonOrder,
                    onClick = { onPokemonClick(pokemon.id, SolidColor(getPokemonBackgroundColor(pokemon.type1))) }
                )
            }

            item {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                } else {
                    LaunchedEffect(gridState.layoutInfo.visibleItemsInfo.lastOrNull()) {
                        if (
                            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == pokemonList.size
                            && !hasReachedEnd
                            ) {
                            viewModel.loadNextPage()
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(targetIndex) {
        targetIndex?.let {
            gridState.scrollToItem(it)
            targetIndex = null
        }
    }
}

@Composable
fun PokeCard(
    pokemon: PokemonEntity?,
    onClick: () -> Unit,
    addZeroToPokemon: (Int) -> String,
    modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        if(pokemon != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = getPokemonBackgroundColor(pokemon.type1),
                ) {
                    AsyncImage(
                        model = pokemon.spriteUrl,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier.height(8.dp))
                Text(
                    text = addZeroToPokemon(pokemon.id),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            CircularProgressIndicator(modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center))
        }
    }

}

@Composable
fun DisplayRegion(region: String, modifier: Modifier = Modifier){
    Row(modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
        ){
        Surface(
            modifier = Modifier
                .width(100.dp)
                .height(20.dp)
            ,
            shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp),
            color = PokedexRed
        ) {

                Text(
                    text = "${stringResource(id = R.string.generation)} ${ region }",
                    style = MaterialTheme.typography.titleSmall,
                    color = PureWhite,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )

        }
        HorizontalDivider(
            thickness = 5.dp,
            color = PokedexRed,
            modifier = Modifier.fillMaxWidth()
        )

    }


}

@Composable
fun RegionFilterRow(
    regionsMap: Map<Int, PokemonRegion>,
    selectedGeneration: Int,
    setGeneration: (Int) -> Unit,
    modifier: Modifier = Modifier){

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(regionsMap.toList()) { (generationId, region) ->
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .clickable {
                        setGeneration(generationId)
                    }
                    .then(
                        if (generationId == selectedGeneration) {
                            Modifier.background(
                                color = PokedexRedTransparant,
                                shape = RoundedCornerShape(15.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = region.regionIcon),
                    contentDescription = region.region,
                    modifier = Modifier.size(50.dp)
                )
                Surface(
                    color = PokedexRed,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(50.dp)
                        .height(20.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = region.region.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DisplayRegionPreview(){
    DisplayRegion(region = "kanto")
}



