package com.example.pokedex.ui.pokemon_detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokedex.models.PokemonMove
import com.example.pokedex.ui.LoadingPokeball
import com.example.pokedex.ui.getPokemonTypeResourceImageId
import com.example.pokedex.ui.theme.LightBackground
import com.example.pokedex.ui.theme.getPokemonBackgroundColor

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
           
                items(moveList) { move ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MoveCard(move = move)
                    }
                    Spacer(modifier = modifier.height(12.dp))

                }
            }
        }
    } else {
        LoadingPokeball()
    }

}

@Composable
fun MoveCard(move: PokemonMove, modifier: Modifier = Modifier){

    var isExpanded by remember {
        mutableStateOf(false)
    }
    val collapseHeight = 150.dp
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (!isExpanded) Modifier.height(collapseHeight) else Modifier.wrapContentHeight())
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = getPokemonBackgroundColor(move.type),
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.Gray
            )
    )
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = move.name.uppercase().replace("-", " "),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "LVL ${move.levelLearned}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier= Modifier.fillMaxWidth()) {
                Image(
                    painterResource(
                        id = getPokemonTypeResourceImageId(move.type)),
                        contentDescription = move.type,
                    modifier = Modifier.size(width = 100.dp, height = 30.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isExpanded) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = move.description,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Power", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = move.power.toString(), fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                VerticalDivider(color = Color.White, modifier = Modifier.height(30.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Accuracy", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = move.accuracy.toString(), fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                VerticalDivider(color = Color.White, modifier = Modifier.height(30.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "PP", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = move.pp.toString(), fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Réduire" else "Voir description",
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )
            }
        }

    }
}