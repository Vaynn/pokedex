package com.example.pokedex.data


import com.google.gson.annotations.SerializedName

data class MoveDetail(
    @SerializedName("accuracy")
    val accuracy: Int,
    @SerializedName("contest_combos")
    val contestCombos: Any,
    @SerializedName("contest_effect")
    val contestEffect: ContestEffect,
    @SerializedName("contest_type")
    val contestType: ContestType,
    @SerializedName("damage_class")
    val damageClass: DamageClass,
    @SerializedName("effect_chance")
    val effectChance: Int,
    @SerializedName("effect_changes")
    val effectChanges: List<Any>,
    @SerializedName("effect_entries")
    val effectEntries: List<EffectEntry>,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntryX>,
    @SerializedName("generation")
    val generation: GenerationXX,
    @SerializedName("id")
    val id: Int,
    @SerializedName("learned_by_pokemon")
    val learnedByPokemon: List<LearnedByPokemon>,
    @SerializedName("machines")
    val machines: List<Any>,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<NameXX>,
    @SerializedName("past_values")
    val pastValues: List<Any>,
    @SerializedName("power")
    val power: Int,
    @SerializedName("pp")
    val pp: Int,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("stat_changes")
    val statChanges: List<StatChange>,
    @SerializedName("super_contest_effect")
    val superContestEffect: SuperContestEffect,
    @SerializedName("target")
    val target: Target,
    @SerializedName("type")
    val type: TypeXXX
)