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
        "unknown" -> R.drawable.ic_hisui_pokeball
        else -> R.drawable.ic_hisui_pokeball
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
        "unknown" -> R.drawable.ic_hisui_pokeball
        else -> R.drawable.ic_hisui_pokeball
    }
}




