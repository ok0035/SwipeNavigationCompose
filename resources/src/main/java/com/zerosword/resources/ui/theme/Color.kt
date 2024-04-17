package com.zerosword.resources.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val unselectedTextColor = Color(0xFFCDCDCD)
val gradientStartColor = Color(0xFF9FE2FF)
val gradientEndColor = Color(0xFFC0CEFF)

val gradientStartColor60 = Color(0x999FE2FF)
val gradientEndColor60 = Color(0x99C0CEFF)

fun Modifier.gradientTextColor(startColor: Color, endColor: Color) = this
    .graphicsLayer(alpha = 0.99f)
    .drawWithCache {
        val brush = Brush.horizontalGradient(listOf(startColor, endColor))
        onDrawWithContent {
            drawContent()
            drawRect(brush, blendMode = BlendMode.SrcAtop)
        }
    }

fun gradientBrush(startColor: Color, endColor: Color) = Brush.horizontalGradient(listOf(startColor, endColor))