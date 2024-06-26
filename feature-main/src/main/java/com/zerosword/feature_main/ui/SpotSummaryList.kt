package com.zerosword.feature_main.ui

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zerosword.domain.model.BubbleModel
import com.zerosword.resources.ui.theme.title16
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


@Composable
fun HorizontalListView(
    modifier: Modifier,
    offset: Float = 0f,
    items: List<BubbleModel> = listOf(
        BubbleModel(id = com.zerosword.resources.R.drawable.test_image, description = "하이하이"),
        BubbleModel(id = com.zerosword.resources.R.drawable.test_image2, description = "하이하이"),
        BubbleModel(id = com.zerosword.resources.R.drawable.test_image3, description = "하이하이"),
        BubbleModel(id = com.zerosword.resources.R.drawable.test_image, description = "하이하이"),
        BubbleModel(id = com.zerosword.resources.R.drawable.test_image2, description = "하이하이")
    )
) {

    Box(modifier = modifier) {
        // LazyRow를 사용하여 수평 스크롤 리스트 생성
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // items 확장 함수를 사용하여 리스트 아이템 반복
            itemsIndexed(items) { index, item ->
                // 각 아이템에 대한 UI 정의
                val isFirst = index == 0
                val startPadding = 20.dp
                val itemWidth = if (isFirst) 104.dp else 88.dp
                val horizontalPadding = 8.dp
                Box(
                    modifier = Modifier
                        .width((if (isFirst) (itemWidth + startPadding) else itemWidth) + horizontalPadding * 2)
                        .wrapContentHeight()
                        .padding(start = if (isFirst) startPadding else 0.dp)
                        .graphicsLayer {
                            translationY = if (index % 2 == 1) {
                                (itemWidth.toPx() / 2.2f) - (itemWidth.toPx() / 20f * index).coerceIn(
                                    0f,
                                    itemWidth.toPx() / 4.4f
                                )
                            } else if (!isFirst) itemWidth.toPx() / 10f else 0f

                            if (index < 5) {
                                translationX =
                                    itemWidth.toPx() * offset * (index + 1) * 1.7f - itemWidth.toPx() * offset * 1.5f
                            }

                        },
                    contentAlignment = Alignment.Center
                ) {

                    val totalDuration = 4000
                    val infiniteTransition = rememberInfiniteTransition(label = "")
                    val bubbleMotion = infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 4.dp.value,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = totalDuration,
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        ), label = ""
                    )

                    BubbleItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                if (index % 2 == 1)
                                    translationX = bubbleMotion.value
                                else
                                    translationY = bubbleMotion.value
                            },
                        title = item.title, res = item.id,
                        diameter = itemWidth
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BubbleItem(modifier: Modifier = Modifier, title: String, res: Int, diameter: Dp) {

    Box(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(diameter, diameter)
                    .graphicsLayer {
//                        translationX = imageCircleHorizontalPadding.value
                    }
                    .clip(CircleShape)
                    .background(Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Cyan),
                    model = res,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(Color.Transparent)
                    .graphicsLayer {
                        translationY = -(49.dp.value)
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                CurvedTextExample()
            }
        }
    }
}

@Composable
fun CurvedTextExample(
    text: String = "그라운드시소 서촌",
    textSize: TextUnit = 14.sp,
) {
    val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        this.textSize = with(LocalDensity.current) { textSize.toPx() }
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    Canvas(
        modifier = Modifier
            .width(textSize.value.dp * (text.length))
            .height(textSize.value.dp * 2)
            .background(Color.Transparent)
    ) {
        val path = Path().apply {
            moveTo(textSize.value, size.height / 2) // 시작점
            quadTo(
                size.width / 2, // 제어점 X
                size.height,     // 제어점 Y (이 값을 변경하여 곡선의 높이 조절)
                size.width, // 끝점 X
                size.height / 2   // 끝점 Y
            )
        }

        drawIntoCanvas {
            it.nativeCanvas.drawTextOnPath(text, path, 0f, 0f, paint)
        }
    }
}

@Preview
@Composable
fun CommonPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        val height = LocalContext.current.resources.configuration.screenHeightDp
        Column(modifier = Modifier.fillMaxSize()) {
            ListTitle(title = "맛집", height = height * 0.08f)
            HorizontalListView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height.dp * 0.202f)
            )
            ListTitle(title = "카페", height = height * 0.08f)
            HorizontalListView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height.dp * 0.202f)
            )
            ListTitle(title = "놀거리", height = height * 0.08f)
            HorizontalListView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height.dp * 0.202f)
            )
        }

    }

}

@Composable
fun ListTitle(modifier: Modifier = Modifier, title: String, height: Float) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(start = 18.dp, bottom = 16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(text = title, style = title16)
    }
}