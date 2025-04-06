package com.felicks.sirbo.ui.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicks.sirbo.data.models.otpModels.routes.RoutesItem
import com.felicks.sirbo.data.models.rutaTest
import com.felicks.sirbo.utils.StringUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailsTopBar(
    route: RoutesItem,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val titulo = StringUtils.extractRouteType(route.longName)
    val direccion = StringUtils.extractRouteDirection(route.longName)

    // 🔥 Estado para el botón de favorito (simulación)
    var isFavorite by remember { mutableStateOf(false) }

    // 🔥 Animación de color al hacer scroll (para futuro uso con LazyColumn)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp) // 🔥 Tamaño más grande para mejor UX
                    .clip(CircleShape) // 🔥 Efecto de botón circular
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)) // 🔥 Fondo sutil
                    .animateContentSize() // 🔥 Animación al cambiar el tamaño
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = direccion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            // 🔥 Botón de favorito con animación
//            IconButton(onClick = { isFavorite = !isFavorite }) {
//                Icon(
//                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
//                    contentDescription = if (isFavorite) "Eliminar de favoritos" else "Añadir a favoritos",
//                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
//            containerColor = Colors.Red,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant, // 🔥 Cambia de color al hacer scroll
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = scrollBehavior // 🔥 Permite animación al hacer scroll
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

