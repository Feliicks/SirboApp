package com.felicksdev.onlymap.presentation.components.topBars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem

@ExperimentalMaterial3Api
@Composable
fun RouteDetailsTopBar(
    route: RoutesModelItem,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(text = "Detalles de la Ruta: ${route.shortName}")
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}