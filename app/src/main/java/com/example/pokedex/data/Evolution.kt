package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class Evolution(
    @SerializedName("baby_trigger_item")
    val babyTriggerItem: Any,
    @SerializedName("chain")
    val chain: Chain,
    @SerializedName("id")
    val id: Int
)