package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class EvolutionDetailX(
    @SerializedName("gender")
    val gender: Any,
    @SerializedName("held_item")
    val heldItem: Any,
    @SerializedName("item")
    val item: ItemX,
    @SerializedName("known_move")
    val knownMove: Any,
    @SerializedName("known_move_type")
    val knownMoveType: Any,
    @SerializedName("location")
    val location: Any,
    @SerializedName("min_affection")
    val minAffection: Any,
    @SerializedName("min_beauty")
    val minBeauty: Any,
    @SerializedName("min_happiness")
    val minHappiness: Any,
    @SerializedName("min_level")
    val minLevel: Any,
    @SerializedName("needs_overworld_rain")
    val needsOverworldRain: Boolean,
    @SerializedName("party_species")
    val partySpecies: Any,
    @SerializedName("party_type")
    val partyType: Any,
    @SerializedName("relative_physical_stats")
    val relativePhysicalStats: Any,
    @SerializedName("time_of_day")
    val timeOfDay: String,
    @SerializedName("trade_species")
    val tradeSpecies: Any,
    @SerializedName("trigger")
    val trigger: TriggerX,
    @SerializedName("turn_upside_down")
    val turnUpsideDown: Boolean
)