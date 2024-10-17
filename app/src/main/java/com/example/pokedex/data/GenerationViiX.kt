package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class GenerationViiX(
    @SerializedName("lets-go-pikachu-lets-go-eevee")
    val letsGoPikachuLetsGoEevee: LetsGoPikachuLetsGoEevee,
    @SerializedName("sun-moon")
    val sunMoon: SunMoon,
    @SerializedName("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: UltraSunUltraMoonX
)