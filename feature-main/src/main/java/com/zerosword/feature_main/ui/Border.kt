package com.tenfingers.feature_popick.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.zerosword.resources.ui.theme.gradientEndColor60
import com.zerosword.resources.ui.theme.gradientStartColor60

@Composable
fun GradientBorderBox(
    modifier: Modifier = Modifier,
    outerPaddingValues: PaddingValues = PaddingValues(0.dp),
    innerPaddingValues: PaddingValues = PaddingValues(0.dp),
    enableBorder: Boolean = true,
    onClick: () -> Unit = {},
    child: @Composable () -> Unit = {}
) {
    // 그라디언트 정의
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(gradientStartColor60, gradientEndColor60)
    )

    fun Modifier.borderModifier(enable: Boolean) = if(enable) Modifier.border(1.5.dp, gradientBrush, RoundedCornerShape(99.dp)) else Modifier
    // Box로 그라디언트 테두리 생성
    Box(
        modifier = modifier
            .padding(outerPaddingValues)
            .borderModifier(enableBorder)
            .padding(innerPaddingValues) // 테두리 내부의 컨텐츠에 대한 패딩
            .clickable { onClick() }
    ) {
        child()
    }

}