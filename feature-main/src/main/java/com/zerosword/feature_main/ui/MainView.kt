package com.zerosword.feature_main.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.CIRCLE_DIAMETER_HEIGHT_RATE1
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.CIRCLE_DIAMETER_HEIGHT_RATE2
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.CIRCLE_DIAMETER_WIDTH_RATE1
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.CIRCLE_DIAMETER_WIDTH_RATE2
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.circleTranslationX1
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.circleTranslationX2
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.circleTranslationY1
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.circleTranslationY2
import com.zerosword.feature_main.ui.animation.MainSpotCircleAnimation.infiniteRotation
import com.zerosword.feature_main.ui.animation.MainSpotDragAnimation.dragFromMainSpotToSpotSummary
import com.zerosword.feature_main.ui.animation.MainSpotDragAnimation.dragFromSpotSummaryToMainSpot
import com.zerosword.feature_main.ui.animation.SpotSummaryListAnimation.animateMainToSpotSummaryList
import com.zerosword.feature_main.ui.animation.SpotSummaryListAnimation.animateSpotSummaryListToMain
import com.zerosword.feature_main.ui.shape.BottomArcShape
import com.zerosword.resources.R
import com.zerosword.resources.ui.theme.body14
import com.zerosword.resources.ui.theme.gradientTextColor
import com.zerosword.resources.ui.theme.title14
import com.zerosword.resources.ui.theme.title24
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainView() {

    val tabIndex = remember {
        mutableIntStateOf(0)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val width = context.resources.configuration.screenWidthDp.dp
    val tabTopPadding = 12.dp
    val tabBarHeight = 40.dp + tabTopPadding
    val offsetX by remember { mutableStateOf(Animatable(width.value)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Main(
            Modifier
                .fillMaxSize(),
            tabIndex,
            offsetX
        )

        val tabBarAlpha = 1f - offsetX.value / width.value
        ScrollableCustomTabBarExample(
            modifier = Modifier
                .height(tabBarHeight)
                .alpha(tabBarAlpha),
            padding = PaddingValues(top = tabTopPadding),
            selectedTabIndexState = tabIndex,
        ) {
            when (it) {
                0 -> {
                    scope.launch {
                        offsetX.snapTo(width.value)
                    }
                }

                1 -> {
                    scope.launch {
                        offsetX.snapTo(0f)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
//@Preview(showBackground = true)
@Composable
fun Main(
    modifier: Modifier = Modifier,
    tabIndexState: MutableIntState = mutableIntStateOf(0),
    offsetX: Animatable<Float, AnimationVector1D>,
    tabBarHeight: Dp = 40.dp
) {

    ConstraintLayout(modifier) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val width = context.resources.configuration.screenWidthDp.dp
        val height = context.resources.configuration.screenHeightDp.dp

        val horizontalListOffset by remember { mutableStateOf(Animatable(0f)) }

        var dragStartX by remember { mutableFloatStateOf(0f) }
        var dragEndX by remember { mutableFloatStateOf(0f) }
        var isDragEnded by remember { mutableStateOf(false) }
        val spotInfoTextBox = createRef()

        val progress = offsetX.value / width.value

        BoxWithConstraints(
            modifier = Modifier
                .width(width * 10)
                .height(height * 10)
        ) {

            LaunchedEffect(key1 = isDragEnded, Dispatchers.IO) {
                if (dragEndX < 0) {
                    horizontalListOffset.animateMainToSpotSummaryList(progress) {
                        tabIndexState.intValue = 1
                    }

                } else {
                    horizontalListOffset.animateSpotSummaryListToMain(progress) {
                        tabIndexState.intValue = 0
                    }
                }
            }

            LaunchedEffect(key1 = isDragEnded, Dispatchers.IO) {

                if (dragEndX < 0) {

                    offsetX.dragFromMainSpotToSpotSummary(progress) {
                        isDragEnded = false
                    }

                } else {

                    offsetX.dragFromSpotSummaryToMainSpot(maxWidth.value, progress) {
                        isDragEnded = false
                    }

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
                                    val newOffset =
                                        (offsetX.value + (dragAmount / 1.5f))
                                            .coerceIn(0f, maxWidth.value)

                                    println("offset -> ${newOffset.dp.toPx()}")
                                    offsetX.snapTo(newOffset)
                                    change.consume()
                                }
                            }
                        )

                    },
                contentAlignment = Alignment.TopCenter,
            ) {

                DrawCircle(modifier = Modifier
                    .wrapContentSize()
                    .graphicsLayer {
                        translationX = MainSpotCircleAnimation.circleTranslationX2(width)
                        translationY = MainSpotCircleAnimation.circleTranslationY2(width, height)
                    }
                    .background(Color.Transparent),
                    widthRate = CIRCLE_DIAMETER_WIDTH_RATE2,
                    heightRate = CIRCLE_DIAMETER_HEIGHT_RATE2,
                    color = Color(0xFFDBE9FF),
                    alpha = (progress).coerceIn(0f, 1f)
                )

                DrawCircle(
                    modifier = Modifier
                        .wrapContentSize()
                        .graphicsLayer {
                            translationX = MainSpotCircleAnimation.circleTranslationX1(width)
                            translationY = MainSpotCircleAnimation.circleTranslationY1(width, height)
                        }
                        .background(Color.Transparent),
                    CIRCLE_DIAMETER_WIDTH_RATE1,
                    CIRCLE_DIAMETER_HEIGHT_RATE1,
                    color = Color(0xFFE4F7FF),
                    alpha = (progress).coerceIn(0f, 1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)// 여기서는 Box의 크기를 지정해줍니다.
                        .clip(BottomArcShape(1f - progress))
                        .background(
                            color = Color.Transparent,
                            shape = BottomArcShape(1f - progress)
                        )
                ) {
                    // Box 내부에 들어갈 내용
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .alpha((progress).coerceIn(0f, 1f)),
                        state = rememberPagerState { 3 },
                        pageSpacing = 0.dp,
                        userScrollEnabled = true,
                        reverseLayout = false,
                        contentPadding = PaddingValues(0.dp),
                        beyondBoundsPageCount = 0,
                        pageSize = PageSize.Fill,
                        key = null,
                        pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                            orientation = Orientation.Horizontal
                        ),
                        pageContent = {
                            GlideImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(Color.Transparent)
                                    .alpha((progress).coerceIn(0f, 1f)),
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
                    (1f - ((width.value - offsetX.value) / (width.value * 0.416f))).coerceIn(0f, 1f)
                )
        ) {
            MainSpotTextBox()
        }

        SummaryView(
            width = width,
            height = height,
            tabBarHeight = tabBarHeight,
            offset = offsetX.value,
            elementOffset = horizontalListOffset.value
        )
    }
}

@Composable
fun SummaryView(
    width: Dp,
    height: Dp,
    tabBarHeight: Dp = 40.dp,
    offset: Float,
    elementOffset: Float = 0f
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = tabBarHeight)
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
    val angle = infiniteTransition.infiniteRotation()

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