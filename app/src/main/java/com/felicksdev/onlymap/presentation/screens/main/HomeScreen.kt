package com.felicksdev.onlymap.presentation.screens.main


import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.plus
import com.felicksdev.onlymap.presentation.components.MyMap
import com.felicksdev.onlymap.presentation.components.RouterPlannerBar
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel
import com.google.maps.android.compose.CameraPositionState

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController,
    cameraPositionState: CameraPositionState,
    plannerViewModel: PlannerViewModel,
    bottomPadding: PaddingValues
) {
    Scaffold(
        topBar = {
            RouterPlannerBar(
                plannerViewModel = plannerViewModel,
                navController = navController
            )
        }) { padding ->
        val fullPading = bottomPadding.plus(padding)
        HomeScreenContent(
            padding = fullPading,
            viewModel = viewModel,
            cameraPositionState = cameraPositionState,
        )
    }
}

@Composable
fun HomeScreenContent(
    padding: PaddingValues,
    viewModel: HomeScreenViewModel,
    cameraPositionState: CameraPositionState,
) {
    MyMap(
        cameraPositionState = cameraPositionState,
        padding = padding
    )
    LaunchedEffect(viewModel.rutaData.value) {
        Log.d("HomeScreen", "Ruta: ${viewModel.rutaData}")
        viewModel.rutaData?.let { ruta ->
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(
        viewModel = HomeScreenViewModel(), // Instancia de ViewModel de prueba
        navController = navController,
        cameraPositionState = CameraPositionState(),
        plannerViewModel = PlannerViewModel(),
        bottomPadding = PaddingValues()
    )
}

