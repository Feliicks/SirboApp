package com.felicksdev.onlymap.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.data.models.rutaTest
import com.felicksdev.onlymap.utils.StringUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailsTopBar(
    route: RoutesItem,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val titulo = StringUtils.extractRouteType(route.longName)
    val direccion = StringUtils.extractRouteDirection(route.longName)

    Log.d("RouteDetailsTopBar", "RouteDetailsTopBar $route")

    CenterAlignedTopAppBar( // 🔥 Material 3 - Mejor alineación del título
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface // 🔥 Usa el color del tema
                )
            }
        },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium, // 🔥 Usa la tipografía del tema
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = direccion,
                    style = MaterialTheme.typography.bodySmall, // 🔥 Usa una tipografía más pequeña
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface, // 🔥 Color de fondo dinámico
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface, // 🔥 Color del icono
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
fun RouteTopBarPreview() {
    RouteDetailsTopBar(
        route = rutaTest,
        modifier = Modifier,
        navController = NavController(LocalContext.current)
    )
}

