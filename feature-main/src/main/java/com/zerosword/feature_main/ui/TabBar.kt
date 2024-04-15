package com.zerosword.feature_main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableCustomTabBarExample(modifier: Modifier, selectedTabIndexState: MutableIntState) {
    val tabs = listOf("Home", "List")

    Surface(modifier) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndexState.intValue,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, text ->
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { selectedTabIndexState.intValue = index }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = text, color = if (selectedTabIndexState.intValue == index) Color.Gray else Color.LightGray)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TabBarPreview() {
    ScrollableCustomTabBarExample(
        Modifier
            .fillMaxSize()
            .height(40.dp), remember {
            mutableIntStateOf(0)
        }
    )
}