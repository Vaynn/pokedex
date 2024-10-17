package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class GenerationIiiX(
    @SerializedName("colosseum")
    val colosseum: Colosseum,
    @SerializedName("emerald")
    val emerald: EmeraldX,
    @SerializedName("firered-leafgreen")
    val fireredLeafgreen: FireredLeafgreenX,
    @SerializedName("ruby-saphire")
    val rubySaphire: RubySaphire,
    @SerializedName("xd")
    val xd: Xd
)