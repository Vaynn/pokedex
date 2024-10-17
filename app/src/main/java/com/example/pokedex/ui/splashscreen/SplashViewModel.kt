package com.example.pokedex.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.network.PokeApiService
import com.example.pokedex.room.dao.PokemonDao
import com.example.pokedex.room.dao.PokemonShortDao
import com.example.pokedex.room.entity.PokemonShortEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val pokemonShortDao: PokemonShortDao,
    private val apiService: PokeApiService
) : ViewModel() {

    val isLoading = MutableStateFlow(true)

    init{
        initializeDatabaseIfEmpty()
    }

    private fun initializeDatabaseIfEmpty(){
        viewModelScope.launch {
            println("initialize----------------")
            isLoading.value = true
            if (pokemonShortDao.getCount() == 0){
                println("getCount() == 0---------------")
                downloadAndStorePokemonData()

            }
            isLoading.value = false
        }
    }
    private suspend fun downloadAndStorePokemonData(){
        try {
            val response = apiService.getPokemonList(20000, 0)
            if (response.isSuccessful && response.body() != null) {
                val pokeList: MutableList<PokemonShortEntity> = mutableListOf()
                response.body()?.results?.forEachIndexed { index, it ->
                    pokeList.add(
                        PokemonShortEntity(
                            id = index + 1,
                            name = it.name,
                            url = it.url
                        )
                    )
                }
                pokemonShortDao.insertAll(pokeList)
                val count = pokemonShortDao.getCount()
                println("Inserted $count Pokémon")
            } else {
                println("API error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("ERROR downloading data : ${e.message}")
        }
    }

}