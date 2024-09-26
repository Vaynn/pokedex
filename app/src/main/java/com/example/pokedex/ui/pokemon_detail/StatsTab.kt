package com.example.pokedex.ui.pokemon_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.R
import com.example.pokedex.data.Pokemon
import com.example.pokedex.ui.theme.DarkGray
import com.example.pokedex.ui.theme.TotalStatsColor
import com.example.pokedex.ui.theme.getStatsColor

@Composable
fun BaseStats(pokemon: Pokemon, modifier: Modifier = Modifier) {

    var total: Int = 0
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

        pokemon.stats.forEach { stat ->
            StatBar(
                statName = stat.stat.name,
                statValue = stat.baseStat,
                statMaxValue = 300 ,
                statColor = getStatsColor(stat.stat.name)
            )
            Spacer(modifier = Modifier.height(12.dp))
            total += stat.baseStat
        }
        StatBar(
            statName = stringResource(id = R.string.total),
            statValue = total,
            statMaxValue = 5000 ,
            statColor = TotalStatsColor
        )
    }

}

@Composable
fun StatBar(statName: String, statValue: Int, statMaxValue: Int, statColor: Color, modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = statName.uppercase().replace("SPECIAL-", "SP. "),
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = modifier.width(8.dp))

        Text(
            text = statValue.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            modifier = Modifier.width(30.dp)
        )

        Spacer(modifier = modifier.width(4.dp))

        Box(
            modifier = Modifier
                .height(12.dp) // Hauteur fixe pour la barre horizontale
                .weight(1f)
                .background(Color.LightGray.copy(alpha = 0.3f)) // Couleur de fond
        ) {
            // Barre de progression en fonction de la valeur
            Box(
                modifier = Modifier
                    .fillMaxHeight() // Prendre toute la hauteur
                    .fillMaxWidth(
                        (statValue / statMaxValue.toFloat()).coerceIn(
                            0f,
                            1f
                        )
                    ) // Ajuster la largeur en fonction de la progression
                    .background(statColor) // Couleur de la barre de progression
            )
        }
    }
}
