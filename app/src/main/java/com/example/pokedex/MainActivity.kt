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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.data.Home
import com.example.pokedex.ui.PokeTopAppBar
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.ui.home.HomeScreen
import com.example.pokedex.ui.splashscreen.SplashScreen
import com.example.pokedex.ui.splashscreen.SplashViewModel
import com.example.pokedex.ui.theme.MetalRedGradient
import com.example.pokedex.ui.theme.PokedexRed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel: PokemonSharedViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                splashViewModel.isLoading.value
            }
        }
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                val isLoading by splashViewModel.isLoading.collectAsState()
                val errorMessage by sharedViewModel.errorMessage.collectAsState()
                val navController = rememberNavController()
                val selectedColor by sharedViewModel.selectedPokemonColor
                val currentDestination by navController.currentBackStackEntryAsState()
                val showBackButton = rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(currentDestination) {
                    showBackButton.value = currentDestination?.destination?.route != NavRoutes.HOME
                    println("Current destination: ${currentDestination?.destination?.route}, Show back button: ${showBackButton.value}")
                }

                if (isLoading){
                    SplashScreen()
                } else {
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
                            if (errorMessage == null && !showBackButton.value ) {
                                FloatingActionButton(
                                    onClick = { navController.navigate(NavRoutes.SEARCH) },
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
}

