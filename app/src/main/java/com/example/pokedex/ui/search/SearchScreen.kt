package com.example.pokedex.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.pokedex.R
import com.example.pokedex.room.entity.PokemonEntity
import com.example.pokedex.ui.CenterTitleDivider
import com.example.pokedex.ui.LoadingPokeball
import com.example.pokedex.ui.home.PokeCard
import com.example.pokedex.ui.specialsPokemonCategoryImageMap
import com.example.pokedex.ui.theme.DarkBackground
import com.example.pokedex.ui.theme.GrassGreen
import com.example.pokedex.ui.theme.PokedexRed
import com.example.pokedex.ui.theme.getPokemonBackgroundColor
import com.example.pokedex.ui.typeImageMap

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onPokemonClick: (Int, Brush) -> Unit,
    modifier: Modifier = Modifier){

    val searchResult by viewModel.searchResult.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val gridState = rememberLazyGridState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = query,
            onQueryChange = { viewModel.onQueryChanged(it) },
            state = gridState
        )

        when {
            isLoading -> {
                LoadingPokeball()
            }
            searchResult.isNotEmpty() -> {
                println("Displaying ${searchResult.size} Pokémon")
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    state = gridState
                ) {
                    items(
                        searchResult,
                        key = { it.id }
                    ) { pokemon ->
                        val appBarBackgroundColor = SolidColor(getPokemonBackgroundColor(pokemon.type1))
                        PokeCard(
                            pokemon = pokemon,
                            addZeroToPokemon = viewModel::addZerosToPokemonOrder,
                            onClick = { onPokemonClick(pokemon.id, appBarBackgroundColor) }
                        )
                    }
                }
            }
            query.isBlank() -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ){
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        CenterTitleDivider(title = stringResource(id = R.string.special).uppercase())
                    }

                    items(specialsPokemonCategoryImageMap.toList()){ (name, image) ->
                        CategoryGridCell(name = name, image = image, onClick = { viewModel.searchPokemonByType(name) } )
                    }

                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                       CenterTitleDivider(title = stringResource(id = R.string.type).uppercase())
                    }

                    items(typeImageMap.toList()) { (name, image) ->
                        CategoryGridCell(name = name, image = image, onClick = { viewModel.searchPokemonByType(name)})
                    }
                }
            }
            searchResult.isEmpty()-> {
                Image(
                    painterResource(id = R.drawable.no_sprite) , 
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                    )
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = stringResource(id = R.string.no_pokemon),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PokedexRed
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    state: LazyGridState,
    modifier: Modifier = Modifier
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.isScrollInProgress) {
        if (state.isScrollInProgress) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text(text = stringResource(id = R.string.search_placeHolder)) },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear text")
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    )
}

@Composable
fun CategoryGridCell(
    name: String,
    image: Int,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick(name) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name.uppercase(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBackground
        )
    }
}
