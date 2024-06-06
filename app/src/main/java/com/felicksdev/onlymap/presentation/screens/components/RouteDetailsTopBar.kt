package com.felicksdev.onlymap.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem
import com.felicksdev.onlymap.data.models.rutaTest

fun extractRouteType(longName: String): String {
    val parts = longName.split(Regex("[:\\-]"))
    return if (parts.size > 1) parts[0] else longName
}

fun extractRouteDirection(longName: String): String {
    val parts = longName.split(Regex("[:\\-]"))
    return if (parts.size > 1) parts[1] else longName
}

@Composable
fun RouteDetailsTopBar(
    modifier: Modifier = Modifier,
    route: RoutesModelItem,
//    navController: NavController
) {

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
//                navController.popBackStack()
                                 }, modifier = Modifier) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack, contentDescription = null,
                    tint = Color.White,
                    )
            }
        },
        title = {
            Column {
                Text(extractRouteType(route.longName), color = Color.White)
                Text(extractRouteDirection(route.longName), color = Color.White)
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun RouteTopBarPreview() {
    RouteDetailsTopBar(
        route = rutaTest,
        modifier = Modifier,
//        navController = NavController(LocalContext.current)
    )
}

