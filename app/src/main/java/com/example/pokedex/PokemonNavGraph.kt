package com.example.pokedex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.ui.home.HomeScreen
import com.example.pokedex.ui.home.HomeViewModel
import com.example.pokedex.ui.pokemon_detail.PokemonDetailScreen

object NavRoutes {
    const val HOME = "home"
    const val POKEMON_DETAIL = "pokemon_detail/{pokemonId}"
    const val SEARCH = "search"

    fun pokemonDetail(pokemonId: Int): String {
        return "pokemon_detail/$pokemonId"
    }
}

@Composable
fun PokemonNavGraph(
    navController: NavHostController,
    viewModel: PokemonSharedViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    NavHost(navController = navController, startDestination = NavRoutes.HOME, modifier = modifier) {
        composable(NavRoutes.HOME){
            HomeScreen(onPokemonClick = { pokemonId, pokemonColor ->
                viewModel.updatePokemonColor(pokemonColor)
                    navController.navigate(NavRoutes.pokemonDetail(pokemonId))
            })
        }

        composable(
            route = NavRoutes.POKEMON_DETAIL,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) {backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: return@composable
            PokemonDetailScreen(
                pokemonId = pokemonId,
                navController = navController,
                onPokemonClick = { pokemonId, pokemonColor ->
                    viewModel.updatePokemonColor(pokemonColor)
                    navController.navigate(NavRoutes.pokemonDetail(pokemonId)){
                        popUpTo(NavRoutes.HOME) { inclusive = false }
                    }

                }
            )
        }
        composable(NavRoutes.SEARCH){

        }
    }
}