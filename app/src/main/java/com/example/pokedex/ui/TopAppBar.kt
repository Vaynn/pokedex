package com.example.pokedex.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pokedex.R
import com.example.pokedex.ui.theme.ElectricYellow
import com.example.pokedex.ui.theme.MetalRedGradient
import com.example.pokedex.ui.theme.WaterBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeTopAppBar(
    title : String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    backgroundColor: Brush,
    modifier: Modifier = Modifier){

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White
                )
            },
            navigationIcon = {
                if (showBackButton){
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White
            ),
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search),
                        tint = Color.White,
                        modifier = Modifier
                            .background(WaterBlue, CircleShape)
                            .size(24.dp)

                    )

                }
            },
            scrollBehavior = scrollBehavior,
            modifier = modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .windowInsetsPadding(WindowInsets.statusBars)
        )

}