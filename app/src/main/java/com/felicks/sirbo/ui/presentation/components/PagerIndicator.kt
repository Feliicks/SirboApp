package com.felicks.sirbo.ui.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 8.dp,
    selectedIndicatorSize: Dp = 12.dp,
    indicatorSpacing: Dp = 8.dp,
    activeColor: Color = Color.DarkGray,
    inactiveColor: Color = Color.LightGray
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val size by animateDpAsState(
                targetValue = if (isSelected) selectedIndicatorSize else indicatorSize,
                label = "indicatorAnim"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = indicatorSpacing / 2)
                    .size(size)
                    .clip(CircleShape)
                    .background(if (isSelected) activeColor else inactiveColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagerIndicatorPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PagerIndicator(
            pageCount = 5,
            currentPage = 2, // simulamos que estamos en la página 2
            modifier = Modifier.padding(16.dp)
        )
    }
}
