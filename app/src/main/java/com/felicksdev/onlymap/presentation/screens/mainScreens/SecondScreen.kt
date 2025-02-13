package com.felicksdev.onlymap.presentation.screens.mainScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Preview
@Composable
fun SecondScreen(
    navController: NavController,
    bottomPadding: PaddingValues
) {

        Column(modifier = Modifier.padding(bottomPadding)) {
            Text(text = "Soy la second Screen")
        }




}