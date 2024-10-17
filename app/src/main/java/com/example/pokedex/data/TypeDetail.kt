package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class TypeDetail(
    @SerializedName("damage_relations")
    val damageRelations: DamageRelations,
    @SerializedName("game_indices")
    val gameIndices: List<GameIndiceX>,
    @SerializedName("generation")
    val generation: GenerationXXXX,
    @SerializedName("id")
    val id: Int,
    @SerializedName("move_damage_class")
    val moveDamageClass: MoveDamageClass,
    @SerializedName("moves")
    val moves: List<MoveXXX>,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<NameXXX>,
    @SerializedName("past_damage_relations")
    val pastDamageRelations: List<Any>,
    @SerializedName("pokemon")
    val pokemon: List<PokemonXX>,
    @SerializedName("sprites")
    val sprites: SpritesX
)