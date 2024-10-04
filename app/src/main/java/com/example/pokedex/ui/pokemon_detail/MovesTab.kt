package com.example.pokedex.ui.pokemon_detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MovesTab(viewModel: PokemonDetailViewModel = hiltViewModel(), modifier: Modifier = Modifier){

    val moves by viewModel.moves.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPokemonMoves()
    }
    if (!isLoading) {
        val moveList = moves
        moveList?.let {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Name", modifier = Modifier.weight(1f))
                        Text(text = "Type", modifier = Modifier.weight(1f))
                        Text(text = "Description", modifier = Modifier.weight(3f))
                        Text(text = "PP", modifier = Modifier.weight(1f))
                        Text(text = "Power", modifier = Modifier.weight(1f))
                        Text(text = "Level", modifier = Modifier.weight(1f))
                    }
                }
                items(moveList) { move ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = move.name, modifier = Modifier.weight(1f))
                        Text(text = move.type, modifier = Modifier.weight(1f))
                        Text(text = move.description, modifier = Modifier.weight(3f))
                        Text(text = move.pp.toString(), modifier = Modifier.weight(1f))
                        Text(text = move.power?.toString() ?: "N/A", modifier = Modifier.weight(1f))
                        Text(text = move.levelLearned.toString(), modifier = Modifier.weight(1f))

                    }

                }
            }
        }
    }

}