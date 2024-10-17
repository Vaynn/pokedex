package com.example.pokedex.ui

import android.os.Build.VERSION.SDK_INT
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.ui.theme.BugType
import com.example.pokedex.ui.theme.DarkGray
import com.example.pokedex.ui.theme.DarkType
import com.example.pokedex.ui.theme.DragonType
import com.example.pokedex.ui.theme.ElectricType
import com.example.pokedex.ui.theme.FairyType
import com.example.pokedex.ui.theme.FightingType
import com.example.pokedex.ui.theme.FireType
import com.example.pokedex.ui.theme.FlyingType
import com.example.pokedex.ui.theme.GhostType
import com.example.pokedex.ui.theme.GrassType
import com.example.pokedex.ui.theme.GroundType
import com.example.pokedex.ui.theme.IceType
import com.example.pokedex.ui.theme.NormalType
import com.example.pokedex.ui.theme.PoisonType
import com.example.pokedex.ui.theme.PokedexRed
import com.example.pokedex.ui.theme.PokedexRedTransparant
import com.example.pokedex.ui.theme.PsychicType
import com.example.pokedex.ui.theme.RockType
import com.example.pokedex.ui.theme.SteelType
import com.example.pokedex.ui.theme.StellarType
import com.example.pokedex.ui.theme.UnknownType
import com.example.pokedex.ui.theme.WaterType
import kotlin.random.Random

@Composable
fun PokeToast(text: String, @DrawableRes iconsResource: Int, modifier: Modifier = Modifier){
    Surface(
        color = PokedexRedTransparant,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(50.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(id = iconsResource),
                contentDescription = "",
                tint = DarkGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                color = DarkGray,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@Composable
fun CenterTitleDivider(@StringRes title: Int){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(thickness = 4.dp, color = PokedexRed, modifier = Modifier.weight(1f))
        Text(text = stringResource(id = title), color = DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        HorizontalDivider(thickness = 4.dp, color = PokedexRed, modifier = Modifier.weight(1f))
    }
}
@Composable
fun CenterTitleDivider(title: String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(thickness = 4.dp, color = PokedexRed, modifier = Modifier.weight(1f))
        Text(text = title, color = DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        HorizontalDivider(thickness = 4.dp, color = PokedexRed, modifier = Modifier.weight(1f))
    }
}

@Composable
fun CenterTitleDividerMini(title: String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(thickness = 2.dp, color = PokedexRed, modifier = Modifier.width(12.dp))
        Text(text = title, color = DarkGray, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp), textAlign = TextAlign.Center)
        HorizontalDivider(thickness = 2.dp, color = PokedexRed, modifier = Modifier.width(12.dp))
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun AnimatedBackground(bubbleColor: Color) {
    val ballCount = 10
    val balls = remember { generateBalls(ballCount) }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    balls.forEach { ball ->
        val animatedX = infiniteTransition.animateFloat(
            initialValue = ball.positionX - 100f,
            targetValue = ball.positionX + 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(ball.speed, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        val animatedY = infiniteTransition.animateFloat(
            initialValue = ball.positionY - 50f,
            targetValue = ball.positionY + 50f,
            animationSpec = infiniteRepeatable(
                animation = tween(ball.speed, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = bubbleColor.copy(alpha = 0.3f),
                radius = ball.radius * 2f,
                center = Offset(animatedX.value, animatedY.value)
            )


            drawCircle(
                color = bubbleColor.copy(alpha = 1f),
                radius = ball.radius,
                center = Offset(animatedX.value, animatedY.value)
            )
        }
    }
}

@Composable
fun LoadingPokeball(modifier: Modifier = Modifier){
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.pokeball_loading)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
            contentDescription = "Loading...",
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop
        )
    }
}

data class Ball(
    val positionX: Float,
    val positionY: Float,
    val radius: Float,
    val color: Color,
    val speed: Int
)

fun generateBalls(count: Int): List<Ball> {
    val random = Random(System.currentTimeMillis())
    return List(count) {
        Ball(
            positionX = random.nextFloat() * 1000f, // Largeur de l'écran
            positionY = random.nextFloat() * 1000f, // Hauteur de l'écran
            radius = random.nextFloat() * 30f + 10f, // Taille des boules
            color = Color(
                random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                1f
            ), // Couleur aléatoire
            speed = random.nextInt(2000, 5000) // Vitesse aléatoire entre 2 et 5 secondes
        )
    }
}

fun getPokemonTypeResourceImageId(type: String): Int {
    return when (type) {
        "normal" -> R.drawable.normal
        "fighting" -> R.drawable.fighting
        "flying" -> R.drawable.flying
        "poison" -> R.drawable.poison
        "ground" -> R.drawable.ground
        "rock" -> R.drawable.rock
        "bug" -> R.drawable.bug
        "ghost" -> R.drawable.ghost
        "steel" -> R.drawable.steel
        "fire" -> R.drawable.fire
        "water" -> R.drawable.water
        "grass" -> R.drawable.grass
        "electric" -> R.drawable.electric
        "psychic" -> R.drawable.psychic
        "ice" -> R.drawable.ice
        "dragon" -> R.drawable.dragon
        "dark" -> R.drawable.dark
        "fairy" -> R.drawable.fairy
        "stellar" -> R.drawable.stellar
        "unknown" -> R.drawable.ic_pokeball
        else -> R.drawable.ic_pokeball
    }
}

fun getPokemonMiniTypeResourceImageId(type: String): Int {
    return when (type) {
        "normal" -> R.drawable.normal_mini
        "fighting" -> R.drawable.fighting_mini
        "flying" -> R.drawable.flying_mini
        "poison" -> R.drawable.poison_mini
        "ground" -> R.drawable.ground_mini
        "rock" -> R.drawable.rock_mini
        "bug" -> R.drawable.bug_mini
        "ghost" -> R.drawable.ghost_mini
        "steel" -> R.drawable.steel_mini
        "fire" -> R.drawable.fire_mini
        "water" -> R.drawable.water_mini
        "grass" -> R.drawable.grass_mini
        "electric" -> R.drawable.electric_mini
        "psychic" -> R.drawable.psychic_mini
        "ice" -> R.drawable.ice_mini
        "dragon" -> R.drawable.dragon_mini
        "dark" -> R.drawable.dark_mini
        "fairy" -> R.drawable.fairy_mini
        "stellar" -> R.drawable.stellar
        "unknown" -> R.drawable.ic_pokeball
        else -> R.drawable.ic_pokeball
    }
}

val typeImageMap: Map<String, Int> = mapOf(
    "normal" to R.drawable.normal_mini,
    "fighting" to R.drawable.fighting_mini,
    "flying" to R.drawable.flying_mini,
    "poison" to R.drawable.poison_mini,
    "ground" to R.drawable.ground_mini,
    "rock" to R.drawable.rock_mini,
    "bug" to R.drawable.bug_mini,
    "ghost" to R.drawable.ghost_mini,
    "steel" to R.drawable.steel_mini,
    "fire" to R.drawable.fire_mini,
    "water" to R.drawable.water_mini,
    "grass" to R.drawable.grass_mini,
    "electric" to R.drawable.electric_mini,
    "psychic" to R.drawable.psychic_mini,
    "ice" to R.drawable.ice_mini,
    "dragon" to R.drawable.dragon_mini,
    "dark" to R.drawable.dark_mini,
    "fairy" to R.drawable.fairy_mini,
)

val specialsPokemonCategoryImageMap: Map<String, Int> = mapOf(
    "mythical" to R.drawable.mythical,
    "legendary" to R.drawable.legendary
)

val regionImageMap: Map<Int, Int> = mapOf(
    0 to R.drawable.all_pokeball,
    1 to R.drawable.kanto_safariball,
    2 to R.drawable.johto_fastball,
    3 to R.drawable.hoenn_diveball,
    4 to R.drawable.sinnoh_duskball,
    5 to R.drawable.unova_dreamball,
    6 to R.drawable.kalos_healball,
    7 to R.drawable.alola_beastball,
    8 to R.drawable.galar_quickball,
    9 to R.drawable.paldea_friendball,
)

fun determineGeneration(id: Int): Int {
    return when (id) {
        in 1..151 -> 1 // Kanto
        in 152..251 -> 2 // Johto
        in 252..386 -> 3
        in 387..493 -> 4
        in 494..649 -> 5
        in 650..721 -> 6
        in 722..809 -> 7
        in 810..905 -> 8
        in 906..1025 -> 9
        else -> 0
    }
}

val legendary: Map<String, String> = mapOf(
        "articuno" to "https://pokeapi.co/api/v2/pokemon/144/",
        "zapdos" to "https://pokeapi.co/api/v2/pokemon/145/",
        "moltres" to "https://pokeapi.co/api/v2/pokemon/146/",
        "mewtwo" to "https://pokeapi.co/api/v2/pokemon/150/",
        "raikou" to "https://pokeapi.co/api/v2/pokemon/243/",
        "entei" to "https://pokeapi.co/api/v2/pokemon/244/",
        "suicune" to "https://pokeapi.co/api/v2/pokemon/245/",
        "lugia" to "https://pokeapi.co/api/v2/pokemon/249/",
        "ho-oh" to "https://pokeapi.co/api/v2/pokemon/250/",
        "regirock" to "https://pokeapi.co/api/v2/pokemon/377/",
        "regice" to "https://pokeapi.co/api/v2/pokemon/378/",
        "registeel" to "https://pokeapi.co/api/v2/pokemon/379/",
        "latias" to "https://pokeapi.co/api/v2/pokemon/380/",
        "latios" to "https://pokeapi.co/api/v2/pokemon/381/",
        "kyogre" to "https://pokeapi.co/api/v2/pokemon/382/",
        "groudon" to "https://pokeapi.co/api/v2/pokemon/383/",
        "rayquaza" to "https://pokeapi.co/api/v2/pokemon/384/",
        "uxie" to "https://pokeapi.co/api/v2/pokemon/480/",
        "mesprit" to "https://pokeapi.co/api/v2/pokemon/481/",
        "azelf" to "https://pokeapi.co/api/v2/pokemon/482/",
        "dialga" to "https://pokeapi.co/api/v2/pokemon/483/",
        "palkia" to "https://pokeapi.co/api/v2/pokemon/484/",
        "heatran" to "https://pokeapi.co/api/v2/pokemon/485/",
        "regigigas" to "https://pokeapi.co/api/v2/pokemon/486/",
        "giratina" to "https://pokeapi.co/api/v2/pokemon/487/",
        "cresselia" to "https://pokeapi.co/api/v2/pokemon/488/",
        "cobalion" to "https://pokeapi.co/api/v2/pokemon/638/",
        "terrakion" to "https://pokeapi.co/api/v2/pokemon/639/",
        "virizion" to "https://pokeapi.co/api/v2/pokemon/640/",
        "tornadus" to "https://pokeapi.co/api/v2/pokemon/641/",
        "thundurus" to "https://pokeapi.co/api/v2/pokemon/642/",
        "reshiram" to "https://pokeapi.co/api/v2/pokemon/643/",
        "zekrom" to "https://pokeapi.co/api/v2/pokemon/644/",
        "landorus" to "https://pokeapi.co/api/v2/pokemon/645/",
        "kyurem" to "https://pokeapi.co/api/v2/pokemon/646/",
        "xerneas" to "https://pokeapi.co/api/v2/pokemon/716/",
        "yveltal" to "https://pokeapi.co/api/v2/pokemon/717/",
        "zygarde" to "https://pokeapi.co/api/v2/pokemon/718/",
        "tapu koko" to "https://pokeapi.co/api/v2/pokemon/785/",
        "tapu lele" to "https://pokeapi.co/api/v2/pokemon/786/",
        "tapu bulu" to "https://pokeapi.co/api/v2/pokemon/787/",
        "tapu fini" to "https://pokeapi.co/api/v2/pokemon/788/",
        "cosmog" to "https://pokeapi.co/api/v2/pokemon/789/",
        "cosmoem" to "https://pokeapi.co/api/v2/pokemon/790/",
        "solgaleo" to "https://pokeapi.co/api/v2/pokemon/791/",
        "lunala" to "https://pokeapi.co/api/v2/pokemon/792/",
        "necrozma" to "https://pokeapi.co/api/v2/pokemon/800/",
        "zacian" to "https://pokeapi.co/api/v2/pokemon/888/",
        "zamazenta" to "https://pokeapi.co/api/v2/pokemon/889/",
        "eternatus" to "https://pokeapi.co/api/v2/pokemon/890/",
        "kubfu" to "https://pokeapi.co/api/v2/pokemon/891/",
        "urshifu" to "https://pokeapi.co/api/v2/pokemon/892/",
        "regieleki" to "https://pokeapi.co/api/v2/pokemon/894/",
        "regidrago" to "https://pokeapi.co/api/v2/pokemon/895/",
        "glastrier" to "https://pokeapi.co/api/v2/pokemon/896/",
        "spectrier" to "https://pokeapi.co/api/v2/pokemon/897/",
        "calyrex" to "https://pokeapi.co/api/v2/pokemon/898/",
        "ting-lu" to "https://pokeapi.co/api/v2/pokemon/1002/",
        "chien-pao" to "https://pokeapi.co/api/v2/pokemon/1003/",
        "wo-chien" to "https://pokeapi.co/api/v2/pokemon/1001/",
        "chi-yu" to "https://pokeapi.co/api/v2/pokemon/1004/",
        "koraidon" to "https://pokeapi.co/api/v2/pokemon/1007/",
        "miraidon" to "https://pokeapi.co/api/v2/pokemon/1008/"
        )

        val mythical = mapOf(
            "mew" to "https://pokeapi.co/api/v2/pokemon/151/",
            "celebi" to "https://pokeapi.co/api/v2/pokemon/251/",
            "jirachi" to "https://pokeapi.co/api/v2/pokemon/385/",
            "deoxys" to "https://pokeapi.co/api/v2/pokemon/386/",
            "phione" to "https://pokeapi.co/api/v2/pokemon/489/",
            "manaphy" to "https://pokeapi.co/api/v2/pokemon/490/",
            "darkrai" to "https://pokeapi.co/api/v2/pokemon/491/",
            "shaymin" to "https://pokeapi.co/api/v2/pokemon/492/",
            "arceus" to "https://pokeapi.co/api/v2/pokemon/493/",
            "victini" to "https://pokeapi.co/api/v2/pokemon/494/",
            "keldeo" to "https://pokeapi.co/api/v2/pokemon/647/",
            "meloetta" to "https://pokeapi.co/api/v2/pokemon/648/",
            "genesect" to "https://pokeapi.co/api/v2/pokemon/649/",
            "diancie" to "https://pokeapi.co/api/v2/pokemon/719/",
            "hoopa" to "https://pokeapi.co/api/v2/pokemon/720/",
            "volcanion" to "https://pokeapi.co/api/v2/pokemon/721/",
            "magearna" to "https://pokeapi.co/api/v2/pokemon/801/",
            "marshadow" to "https://pokeapi.co/api/v2/pokemon/802/",
            "zeraora" to "https://pokeapi.co/api/v2/pokemon/807/",
            "meltan" to "https://pokeapi.co/api/v2/pokemon/808/",
            "melmetal" to "https://pokeapi.co/api/v2/pokemon/809/",
            "zarude" to "https://pokeapi.co/api/v2/pokemon/893/",
)


/**
 * add X times 0 to make 4 digit number
 */
fun addZerosToPokemonOrder(order: Int): String{
    return "#${order.toString().padStart(4, '0')}"
}




