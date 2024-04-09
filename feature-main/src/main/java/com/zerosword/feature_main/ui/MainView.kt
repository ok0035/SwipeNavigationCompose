package com.zerosword.feature_main.ui

import android.app.Activity
import android.os.Build
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zerosword.feature_main.viewmodel.MainViewModel
import com.zerosword.resources.R
import com.zerosword.resources.ui.theme.DarkColorScheme
import com.zerosword.resources.ui.theme.LightColorScheme
import com.zerosword.resources.ui.theme.Typography
import com.zerosword.resources.ui.theme.body14
import com.zerosword.resources.ui.theme.gradientTextColor
import com.zerosword.resources.ui.theme.title14
import com.zerosword.resources.ui.theme.title24
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainView(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
) {
    val viewModel: MainViewModel = hiltViewModel()

    SwipeScreen()

}

@Composable
fun TemplateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun SwipeScreen() {

    ConstraintLayout {

        val context = LocalContext.current
        val width = context.resources.configuration.screenWidthDp.dp
        val height = context.resources.configuration.screenHeightDp.dp
        val scope = rememberCoroutineScope()

        val offsetX by remember { mutableStateOf(Animatable(width.value)) }
        var isDragEnded by remember { mutableStateOf(false) }
        var isScrollDirection by remember { mutableFloatStateOf(0f) }

        val spotInfoTextBox = createRef()

        BoxWithConstraints(
            modifier = Modifier
                .width(width * 10)
                .height(height * 10)
        ) {

            LaunchedEffect(key1 = isDragEnded) {
                val currentOffset = offsetX.value
                val progress = currentOffset / maxWidth.value
                val min = maxWidth.value * 0.6f
                val max = maxWidth.value
                val duration =
                    (((currentOffset / maxWidth.value - 0.6f) * 3000).coerceIn(0f, 3000f)).toInt()
                println("current offset -> $progress")

                if (progress < 0.8f)
                    offsetX.animateTo(
                        targetValue = min,
                        animationSpec = tween(
                            durationMillis = duration
                        )
                    ) {
                        isDragEnded = false
                    }
                else {
                    offsetX.animateTo(
                        targetValue = max,
                        animationSpec = tween(
                            durationMillis = duration
                        )
                    ) {
                        isDragEnded = false
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(

                            onDragEnd = {
                                isDragEnded = true

                            },
                            onDragCancel = {
                                isDragEnded = true

                            },
                            onHorizontalDrag = { change, dragAmount ->
                                scope.launch {
                                    val newOffset = (offsetX.value + (dragAmount / 3f)).coerceIn(
                                        0f,
                                        maxWidth.value
                                    )
                                    offsetX.snapTo(newOffset)
                                    change.consume()
                                    println("offset -> ${offsetX.value}")
                                }
                            }
                        )

                    },
                contentAlignment = Alignment.TopCenter,
            ) {

                HorizontalPager(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
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
                                .fillMaxHeight(0.7f)
                                .background(Color.White),
                            model =
                                when(it) {
                                    0 -> R.drawable.test_image
                                    1 -> R.drawable.test_image2
                                    2 -> R.drawable.test_image3
                                    else -> R.drawable.test_image
                                }
                            ,
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.TopCenter,
                            contentDescription = null,
                        )
                    }
                )
            }

            MainClippingCircle(
                Modifier
                    .width(maxWidth * 10)
                    .height(maxHeight * 10), scale = offsetX.value / maxWidth.value
            )


//        Box(
//            modifier = Modifier
//                .requiredWidth(swipeWidth)
//                .fillMaxHeight()
//                .background(Color.Transparent),
//            contentAlignment = Alignment.TopStart
//        ) {
//
//            Row(
//                modifier = Modifier
//                    .requiredWidth(swipeWidth)
//                    .fillMaxHeight()
//                    .offset(x = screenWidth / 2 + offsetX.dp),
//            ) {
//                Box(
//                    modifier = Modifier
//                        .requiredWidth(screenWidth)
//                        .fillMaxHeight()
//                        .background(Color.Transparent)
//                )
//
//                Box(
//                    modifier = Modifier
//                        .width(screenWidth)
//                        .fillMaxHeight()
//                        .background(Color.Blue)
//                )
//            }
//
//        }
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
                    (1f - ((width.value - offsetX.value) / (width.value * 0.6f * 0.416f))).coerceIn(
                        0f,
                        1f
                    )
                )
        ) {
            MainSpotTextBox()
        }
    }
}

@Composable
fun MainClippingCircle(modifier: Modifier, scale: Float) {
    Canvas(
        modifier = modifier
            .background(Color.Transparent)
    ) {
        // 화면의 중앙에 원을 그립니다.
        val newDrawingSize = Size(size.width * 10, size.height * 10)
        val circleDiameter = size.width * 6.38f

        val radius = circleDiameter / 2f * scale

        val centerOffset = Offset(
            0f,
            -(circleDiameter / 2f) + size.height / 1.6f
        )
        // 원형 클리핑 경로 생성
        val clipPath = Path().apply {
            addOval(
                Rect(
                    centerOffset - Offset(radius, radius),
                    centerOffset + Offset(radius, radius)
                )
            )
        }
        clipPath(clipPath, clipOp = ClipOp.Difference) {
            drawRect(
                color = Color.White,
                size = newDrawingSize
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
    SwipeScreen()
}