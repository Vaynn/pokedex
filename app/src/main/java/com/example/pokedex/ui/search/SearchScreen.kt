package com.example.pokedex.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokedex.R
import com.example.pokedex.ui.LoadingPokeball
import com.example.pokedex.ui.home.PokeCard
import com.example.pokedex.ui.theme.PokedexRed
import com.example.pokedex.ui.theme.getPokemonBackgroundColor

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
            query.isBlank() -> {
                Text(text = "Tapez quelque chose pour commencer la recherche")
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
            else -> {
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