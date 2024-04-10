package com.zerosword.feature_main.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

class BottomArcShape(private val offset: Float = 0f) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): Outline {

        val path = Path().apply {
            // 상단 및 좌, 우변을 직선으로 그립니다.
            moveTo(0f, 0f)
            lineTo(0f, size.height - 50.dp.value - offset * size.height) // 곡선 시작 전까지 세로로 이동
            // 곡선을 그립니다.
            quadraticBezierTo(
                x1 = size.width / 2f,
                y1 = size.height - 50.dp.value - offset * size.height, // 곡선의 높이 조절
                x2 = size.width,
                y2 = size.height - 180.dp.value - offset * size.height * 1.15f // 곡선이 끝나는 지점까지 세로로 이동
            )
            lineTo(size.width, 0f) // 상단 오른쪽 모서리로 이동

            close() // 경로를 닫아 사각형 완성
        }
        return Outline.Generic(path)
    }
}