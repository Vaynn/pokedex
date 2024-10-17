package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class PokemonForm(
    @SerializedName("form_name")
    val formName: String,
    @SerializedName("form_names")
    val formNames: List<FormName>,
    @SerializedName("form_order")
    val formOrder: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_battle_only")
    val isBattleOnly: Boolean,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("is_mega")
    val isMega: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<NameXXXX>,
    @SerializedName("order")
    val order: Int,
    @SerializedName("pokemon")
    val pokemon: PokemonXXXX,
    @SerializedName("sprites")
    val sprites: SpritesXX,
    @SerializedName("types")
    val types: List<TypeXXXX>,
    @SerializedName("version_group")
    val versionGroup: VersionGroupXXX
)