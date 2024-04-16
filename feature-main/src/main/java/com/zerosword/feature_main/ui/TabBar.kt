package com.zerosword.feature_main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.zerosword.resources.ui.compose.ResizableScrollableTabRow
import com.zerosword.resources.ui.theme.gradientBrush
import com.zerosword.resources.ui.theme.gradientEndColor
import com.zerosword.resources.ui.theme.gradientStartColor
import com.zerosword.resources.ui.theme.gradientTextColor
import com.zerosword.resources.ui.theme.title14
import com.zerosword.resources.ui.theme.unselectedTextColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ScrollableCustomTabBarExample(
    modifier: Modifier,
    tabBarHeight: Dp = 40.dp,
    selectedTabIndexState: MutableIntState
) {
    val tabs = listOf("Home", "List")
    val arrowWidth = 56.dp
    Box(modifier.fillMaxWidth().height(tabBarHeight).background(Color.Transparent)) {

        ResizableScrollableTabRow(
            modifier = Modifier
                .background(Color.Transparent)
                .wrapContentWidth()
                .padding(start = 40.dp)
                .fillMaxHeight(),
            minItemWidth = 20.dp,
            selectedTabIndex = selectedTabIndexState.intValue,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            indicator = {},
            divider = {},
        ) {
            tabs.forEachIndexed { index, text ->
                Box(
                    modifier = Modifier
                        .clickable { selectedTabIndexState.intValue = index }
                        .background(Color.Transparent)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        modifier =
                        if (selectedTabIndexState.intValue == index)
                            Modifier.gradientTextColor(gradientStartColor, gradientEndColor)
                        else {
                            Modifier.gradientTextColor(unselectedTextColor, unselectedTextColor)
                        }.background(Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 9.dp),
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
                contentDescription = null,
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun TabBarPreview() {
    ScrollableCustomTabBarExample(
        Modifier
            .fillMaxSize()
            .height(40.dp),
        tabBarHeight = 40.dp,
        selectedTabIndexState = remember {
            mutableIntStateOf(0)
        }
    )
}