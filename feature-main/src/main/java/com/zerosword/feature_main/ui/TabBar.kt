package com.zerosword.feature_main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.tenfingers.feature_popick.view.GradientBorderBox
import com.zerosword.resources.ui.compose.ResizableScrollableTabRow
import com.zerosword.resources.ui.theme.gradientBrush
import com.zerosword.resources.ui.theme.gradientEndColor
import com.zerosword.resources.ui.theme.gradientStartColor
import com.zerosword.resources.ui.theme.gradientTextColor
import com.zerosword.resources.ui.theme.title14
import com.zerosword.resources.ui.theme.unselectedTextColor


@Composable
fun ScrollableCustomTabBarExample(
    modifier: Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    selectedTabIndexState: MutableIntState,
    onClick: (index: Int) -> Unit = {}
) {
    val tabs = listOf("메인스팟", "데이트 시작")
    val arrowWidth = 56.dp
    Box(
        modifier
            .fillMaxWidth()
            .padding(padding)
            .background(Color.Transparent)
    ) {

        ResizableScrollableTabRow(
            modifier = Modifier
                .background(Color.Transparent)
                .wrapContentWidth()
                .padding(start = 16.dp)
                .fillMaxHeight(),
            minItemWidth = 0.dp,
            selectedTabIndex = selectedTabIndexState.intValue,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            indicator = {},
            divider = {},
        ) {
            tabs.forEachIndexed { index, text ->

                GradientBorderBox(
                    Modifier
                        .background(Color.Transparent),
                    enableBorder = index == selectedTabIndexState.intValue,
                    outerPaddingValues = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                    innerPaddingValues = PaddingValues(horizontal = 12.dp, vertical = 9.dp),
                    onClick = {
                        selectedTabIndexState.intValue = index
                        onClick(index)
                        println("click")
                    }
                ) {

                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(0.dp)
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .let {
                                if (selectedTabIndexState.intValue == index)
                                    it.gradientTextColor(gradientStartColor, gradientEndColor)
                                else {
                                    it.gradientTextColor(unselectedTextColor, unselectedTextColor)
                                }
                            },
                        textAlign = TextAlign.Center,
                        style = title14
                    )

                }
            }
        }

        Box(
            Modifier
                .width(arrowWidth)
                .fillMaxHeight()
                .background(gradientBrush(Color.White, Color.Transparent)),
            contentAlignment = Alignment.Center
        ) {
            val arrow: ImageVector =
                ImageVector.vectorResource(id = com.zerosword.resources.R.drawable.tab_bar_arrow)
            Image(
                modifier = Modifier
                    .width(6.dp)
                    .wrapContentHeight()
                    .background(Color.Transparent),
                imageVector = arrow,
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
            )
        }

    }
}


//@Preview(showBackground = true)
@Composable
fun TabBarPreview() {
    ScrollableCustomTabBarExample(
        Modifier
            .fillMaxSize()
            .height(40.dp),
        selectedTabIndexState = remember {
            mutableIntStateOf(0)
        }
    )
}