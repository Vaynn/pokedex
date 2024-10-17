package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class GenerationViX(
    @SerializedName("omega-ruby-alpha-sapphire")
    val omegaRubyAlphaSapphire: OmegaRubyAlphaSapphireX,
    @SerializedName("x-y")
    val xY: XYX
)