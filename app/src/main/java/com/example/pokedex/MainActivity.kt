package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.data.Home
import com.example.pokedex.ui.PokeTopAppBar
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui.home.HomeScreen
import com.example.pokedex.ui.theme.MetalRedGradient
import com.example.pokedex.ui.theme.PokedexRed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel: PokemonSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                val  navController = rememberNavController()
                val selectedColor by sharedViewModel.selectedPokemonColor
                val currentDestination by navController.currentBackStackEntryAsState()
                val showBackButton = rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(currentDestination) {
                    showBackButton.value = currentDestination?.destination?.route != NavRoutes.HOME
                }

                Scaffold(
                    topBar = {
                        PokeTopAppBar(
                            title = getString(R.string.app_name),
                            showBackButton = showBackButton.value,
                            backgroundColor = selectedColor,
                            onBackClick = {
                                sharedViewModel.updatePokemonColor(MetalRedGradient)
                                navController.navigateUp()
                            }
                        )
                    },
                    floatingActionButton = {
                        if (!showBackButton.value) {
                            FloatingActionButton(
                                onClick = { /* Handle search action */ },
                                containerColor = PokedexRed
                            )
                            {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = stringResource(id = R.string.search),
                                    tint = Color.White
                                )
                            }
                        }
                    }

                ) { innerPadding ->

                    PokemonNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                        )
                }

            }
        }
    }
}

