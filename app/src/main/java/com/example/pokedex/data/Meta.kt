package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("ailment")
    val ailment: Ailment,
    @SerializedName("ailment_chance")
    val ailmentChance: Int,
    @SerializedName("category")
    val category: Category,
    @SerializedName("crit_rate")
    val critRate: Int,
    @SerializedName("drain")
    val drain: Int,
    @SerializedName("flinch_chance")
    val flinchChance: Int,
    @SerializedName("healing")
    val healing: Int,
    @SerializedName("max_hits")
    val maxHits: Any,
    @SerializedName("max_turns")
    val maxTurns: Any,
    @SerializedName("min_hits")
    val minHits: Any,
    @SerializedName("min_turns")
    val minTurns: Any,
    @SerializedName("stat_chance")
    val statChance: Int
)