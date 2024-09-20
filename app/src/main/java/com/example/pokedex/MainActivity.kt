package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.data.Home
import com.example.pokedex.ui.PokeTopAppBar
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui.home.HomeScreen
import com.example.pokedex.ui.theme.MetalRedGradient
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
                Scaffold(
                    topBar = {
                        PokeTopAppBar(
                            title = getString(R.string.app_name),
                            showBackButton = currentDestination?.destination?.route != NavRoutes.HOME,
                            backgroundColor = selectedColor,
                            onBackClick = {
                                sharedViewModel.updatePokemonColor(MetalRedGradient)
                                navController.navigateUp()
                            }
                        ) }
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

