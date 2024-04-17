package com.zerosword.resources.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val title24 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight(700),
    fontSize = 24.sp,
    color = Color(0xFF333333),
    lineHeight = 33.6.sp,
)

val title20 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight(700),
    color = Color(0xFF333333),
    fontSize = 20.sp,
    lineHeight = 28.sp,
)

val title16 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight(700),
    color = Color(0xFF333333),
    fontSize = 16.sp,
    lineHeight = 16.sp,
)

val title14 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight(700),
    fontSize = 14.sp,
    color = Color(0xFF333333),
    lineHeight = 14.sp,
)

val body14 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight(500),
    color = Color(0xFF4A4A4A),
    fontSize = 14.sp,
    lineHeight = 21.sp,
)

val body12 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight(500),
    fontSize = 12.sp,
    color = Color(0xFF4A4A4A),
    lineHeight = 18.sp,
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)