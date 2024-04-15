package com.zerosword.feature_main.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zerosword.feature_main.ui.shape.BottomArcShape
import com.zerosword.feature_main.viewmodel.MainViewModel
import com.zerosword.resources.R
import com.zerosword.resources.ui.theme.body14
import com.zerosword.resources.ui.theme.gradientTextColor
import com.zerosword.resources.ui.theme.title14
import com.zerosword.resources.ui.theme.title24
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
@Preview(showBackground = true)
fun MainView() {
//    val viewModel: MainViewModel = hiltViewModel()
    val tabIndex = remember {
        mutableIntStateOf(0)
    }
    val tabBarHeight = 40.dp
    Box(modifier = Modifier.fillMaxSize()) {
        MainSpot(
            Modifier
                .fillMaxSize()
                .padding(top = tabBarHeight),
            tabIndex
        )
        ScrollableCustomTabBarExample(Modifier.height(tabBarHeight), tabIndex)
    }

}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
//@Preview(showBackground = true)
@Composable
fun MainSpot(modifier: Modifier, tabIndexState: MutableIntState) {

    ConstraintLayout(modifier) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val width = context.resources.configuration.screenWidthDp.dp
        val height = context.resources.configuration.screenHeightDp.dp

        val offsetX by remember { mutableStateOf(Animatable(width.value)) }
        val imageAlphaOffset by remember { mutableStateOf(Animatable(width.value)) }
        val horizontalListOffset by remember { mutableStateOf(Animatable(0f)) }

        var dragStartX by remember { mutableFloatStateOf(0f) }
        var dragEndX by remember { mutableFloatStateOf(0f) }
        var isDragEnded by remember { mutableStateOf(false) }

        val spotInfoTextBox = createRef()

        BoxWithConstraints(
            modifier = Modifier
                .width(width * 10)
                .height(height * 10)
        ) {

            LaunchedEffect(key1 = isDragEnded, Dispatchers.IO) {
                val totalDuration = 2000
                if (dragEndX < 0) {

                    horizontalListOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = (totalDuration * offsetX.value / width.value).toInt()
                                .coerceIn(
                                    totalDuration / 3,
                                    totalDuration.coerceIn(totalDuration / 3, totalDuration)
                                ),
                            easing = CubicBezierEasing(0.21f, 0.0f, 0.35f, 1.0f)
                        )
                    ) {
                        tabIndexState.intValue  = 1
                    }

                } else {
                    horizontalListOffset.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = ((totalDuration - (totalDuration * offsetX.value / width.value).toInt()) / 2).coerceIn(
                                0,
                                totalDuration
                            ),
                            easing = CubicBezierEasing(0.21f, 0.0f, 0.35f, 1.0f)
                        )
                    ) {
                        tabIndexState.intValue = 0
                    }
                }
            }

            LaunchedEffect(key1 = isDragEnded, Dispatchers.IO) {
                val currentOffset = offsetX.value
                val min = 0f
                val max = maxWidth.value
                val totalDuration = 1000
                val duration =
                    (((currentOffset / maxWidth.value) * totalDuration).coerceIn(
                        0f,
                        totalDuration.toFloat()
                    )).toInt()

//                val distance = abs(dragEndX) - dragStartX
                if (dragEndX < 0) {

                    offsetX.animateTo(
                        targetValue = min,
                        animationSpec = tween(
                            durationMillis = duration,
                            easing = LinearEasing
                        )
                    ) {
                        isDragEnded = false
                    }


                    imageAlphaOffset.animateTo(
                        targetValue = min,
                        animationSpec = tween(
                            durationMillis = (duration / 1.5f).toInt(),
                            easing = LinearEasing
                        )
                    )
                } else {

                    offsetX.animateTo(
                        targetValue = max,
                        animationSpec = tween(
                            durationMillis = (totalDuration - duration) / 2,
                            easing = LinearEasing
                        )
                    ) {
                        isDragEnded = false
                    }

                    imageAlphaOffset.animateTo(
                        targetValue = max,
                        animationSpec = tween(
                            durationMillis = ((totalDuration / 1.5f) - duration / 1.5f).toInt(),
                            easing = LinearEasing
                        )
                    )

                }

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { offset ->
                                dragStartX = offset.x
                                dragEndX = 0f
                            },

                            onDragEnd = {
                                isDragEnded = true
                            },
                            onDragCancel = {
                                isDragEnded = true
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                dragEndX += dragAmount
                                scope.launch {
                                    val newOffset = (offsetX.value + (dragAmount / 1.5f)).coerceIn(
                                        0f,
                                        maxWidth.value
                                    )

                                    val newAlphaOffset =
                                        (imageAlphaOffset.value + dragAmount).coerceIn(
                                            0f,
                                            maxWidth.value
                                        )
                                    println("offset -> ${newOffset.dp.toPx()}")
                                    offsetX.snapTo(newOffset)
                                    imageAlphaOffset.snapTo(newAlphaOffset)
                                    change.consume()
                                }
                            }
                        )

                    },
                contentAlignment = Alignment.TopCenter,
            ) {

                val circle1Width = width * 2.1f
                val circle1Height = width * 2.24f

                val circle2Width = width * 2.17f
                val circle2Height = width * 2.26f

                DrawCircle(modifier = Modifier
                    .wrapContentSize()
                    .graphicsLayer {
                        translationX = circle2Width.value / 2f - circle2Width.value / 1.8f
                        translationY = (height.value * 0.7f) - circle2Height.value * 1.45f
                    }
                    .background(Color.Transparent),
                    widthRate = 2.17f,
                    heightRate = 2.26f,
                    color = Color(0xFFDBE9FF),
                    alpha = (offsetX.value / width.value).coerceIn(0f, 1f)
                )

                DrawCircle(
                    modifier = Modifier
                        .wrapContentSize()
                        .graphicsLayer {
                            translationX = -circle1Width.value / 1.3f
                            translationY = height.value * 0.7f - circle1Height.value * 1.35f
                        }
                        .background(Color.Transparent),
                    2.1f,
                    2.24f,
                    color = Color(0xFFE4F7FF),
                    alpha = (offsetX.value / width.value).coerceIn(0f, 1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)// 여기서는 Box의 크기를 지정해줍니다.
                        .clip(BottomArcShape(1f - offsetX.value / width.value))
                        .background(
                            color = Color.Transparent,
                            shape = BottomArcShape(1f - offsetX.value / width.value)
                        )
                ) {
                    // Box 내부에 들어갈 내용
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .alpha((offsetX.value / width.value).coerceIn(0f, 1f)),
                        state = rememberPagerState { 3 },
                        pageSpacing = 0.dp,
                        userScrollEnabled = true,
                        reverseLayout = false,
                        contentPadding = PaddingValues(0.dp),
                        beyondBoundsPageCount = 0,
                        pageSize = PageSize.Fill,
                        key = null,
                        pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                            Orientation.Horizontal
                        ),
                        pageContent = {
                            GlideImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(Color.Transparent)
                                    .alpha((offsetX.value / width.value).coerceIn(0f, 1f)),
                                model = when (it) {
                                    0 -> R.drawable.test_image
                                    1 -> R.drawable.test_image2
                                    2 -> R.drawable.test_image3
                                    else -> R.drawable.test_image
                                },
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.TopCenter,
                                contentDescription = null,
                            )
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .constrainAs(spotInfoTextBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .offset(
                    x = offsetX.value.dp - width
                )
                .alpha(
                    (1f - ((width.value - offsetX.value) / (width.value * 0.416f))).coerceIn(
                        0f,
                        1f
                    )
                )
        ) {
            MainSpotTextBox()
        }

        SummaryView(width, height, offsetX.value, elementOffset = horizontalListOffset.value)
    }
}

@Composable
fun SummaryView(width: Dp, height: Dp, offset: Float, elementOffset: Float = 0f) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            ListTitle(
                modifier = Modifier
                    .graphicsLayer {
                        translationX =
                            (width.toPx() - (width.toPx() - offset.dp.toPx())) / 4f
                    }
                    .alpha(1f - offset / width.value),
                title = "맛집",
                height = height.value * 0.08f
            )
            HorizontalListView(
                modifier = Modifier
                    .graphicsLayer {
                        translationX =
                            (width.toPx() - (width.toPx() - offset.dp.toPx())) * 1.2f
                    }
                    .fillMaxWidth()
                    .height(height * 0.202f),
                offset = elementOffset
            )
            ListTitle(
                modifier = Modifier
                    .graphicsLayer {
                        translationX =
                            (width.toPx() - (width.toPx() - offset.dp.toPx())) / 4f
                    }
                    .alpha(1f - offset / width.value),
                title = "카페",
                height = height.value * 0.08f
            )
            HorizontalListView(
                modifier = Modifier
                    .graphicsLayer {
                        translationX =
                            (width.toPx() - (width.toPx() - offset.dp.toPx())) * 1.2f
                    }
                    .fillMaxWidth()
                    .height(height * 0.202f),
                offset = elementOffset
            )
            ListTitle(
                modifier = Modifier
                    .graphicsLayer {
                        translationX =
                            (width.toPx() - (width.toPx() - offset.dp.toPx())) / 4f
                    }
                    .alpha(1f - offset / width.value),
                title = "놀거리",
                height = height.value * 0.08f
            )
            HorizontalListView(
                modifier = Modifier
                    .graphicsLayer {
                        translationX =
                            (width.toPx() - (width.toPx() - offset.dp.toPx())) * 0.82f
                    }
                    .fillMaxWidth()
                    .height(height * 0.202f),
                offset = elementOffset
            )
        }
    }
}

@Composable
fun DrawCircle(
    modifier: Modifier = Modifier,
    widthRate: Float,
    heightRate: Float,
    color: Color,
    alpha: Float = 1f
) {

    // 무한 회전 애니메이션을 위한 Transition
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Canvas(modifier = modifier
            .graphicsLayer {
                rotationZ = angle.value
                translationX = -(size.width * (1f - alpha)) * 0.8f
                translationY = -(size.width * (1f - alpha)) * 0.8f
            }
            .fillMaxSize()) {

            // 타원의 실제 크기를 계산합니다.
            val ovalWidth = size.width * widthRate
            val ovalHeight = size.width * heightRate
            // 타원을 Canvas의 중심에 위치시키기 위한 topLeft 계산
            val topLeftX = (size.width - ovalWidth) / 2f
            val topLeftY = (size.height - ovalHeight) / 2f

            // 아크를 그려서 타원을 만듭니다.
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                alpha = alpha,
                topLeft = Offset(topLeftX, topLeftY),
                size = Size(ovalWidth, ovalHeight)
            )
        }
    }

}


@Composable
fun MainSpotTextBox(
    modifier: Modifier = Modifier,
    title: String = "그라운드시소 서촌",
    body: String = "뉴트로하면 떠오르는 대표적 핫플레이스. 수많은 문학가와 예술가가 살았던 서촌은 고풍스러우면서도 레트로한 멋까지 함께 느낄 수 있는 동네다."
) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .padding(start = 28.dp, end = 52.dp, top = 7.dp, bottom = 40.dp)
    ) {
        Column {
            Text(
                modifier = Modifier,
                text = title,
                style = title24,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier,
                text = body,
                style = body14,
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                style = title14,
                modifier = Modifier.gradientTextColor(
                    startColor = Color(0xFF9FE2FF),
                    endColor = Color(0xFFC0CEFF)
                ),
                text = "자세히 보기 >",
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun MainPreview() {
    val context = LocalContext.current
    val configuration = context.resources.configuration
    val width = configuration.screenWidthDp
    val height = configuration.screenHeightDp

//     BoxWithCircle(width = width.dp * 2.21f,  height = height.dp * 2.24f)
//    EllipseWithDrawArc(width.dp * 2.21f, width.dp * 2.24f)
}
