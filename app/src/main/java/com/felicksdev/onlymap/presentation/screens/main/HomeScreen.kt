package com.felicksdev.onlymap.presentation.screens.main


import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.plus
import com.felicksdev.onlymap.presentation.components.LoadingScreen
import com.felicksdev.onlymap.presentation.components.MyMap
import com.felicksdev.onlymap.presentation.components.bottomBars.BottomSheetDetail
import com.felicksdev.onlymap.presentation.components.topBars.SmallRouterPlannerBar
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController,
    myCameraPositionState: CameraPositionState,
    plannerViewModel: PlannerViewModel,
    bottomPadding: PaddingValues
) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    LaunchedEffect(Unit) {
//        si plan es definido y vvalido entonces cargar una loading screen hazstal respuesta de fetchplan
        if (plannerViewModel.isPlacesDefined()) {
            plannerViewModel.fetchPlan()
        }
    }
    val planResult by plannerViewModel.planResult.collectAsState()
    val errorState by plannerViewModel.errorState.collectAsState()
    val isLoading by plannerViewModel.isLoading.collectAsState()
    val cameraPositionState = plannerViewModel.cameraPosition.collectAsState()
    val cameraState = rememberCameraPositionState {
        position = cameraPositionState.value
    }
    Scaffold(
        topBar = {
            SmallRouterPlannerBar(
                plannerViewModel = plannerViewModel,
                navController = navController
            )
        },
        bottomBar = {
                if (plannerViewModel.isPlacesDefined()) {
                    planResult?.let {plan ->
                        BottomSheetDetail(scaffoldState = scaffoldState, itineraries = plan.itineraries)
                    }
                }
//            BottomDetail(scaffoldState = scaffoldState, legs = listOf(Leg(), Leg()))
        }
    ) { padding ->
        val fullPading = bottomPadding.plus(padding)
        HomeScreenContent(
            padding = fullPading,
            viewModel = viewModel,
            cameraPositionState = cameraState,
        )
        // Si está cargando, muestra el indicador de carga

        if (isLoading) LoadingScreen()

        // Mostrar AlertDialog si hay un error
        errorState?.let { errorMessage ->
            ErrorDialog(
                errorMessage = errorMessage,
                onDismiss = { plannerViewModel.clearError() }
            )
        }
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
        myCameraPositionState = CameraPositionState(),
        plannerViewModel = PlannerViewModel(),
        bottomPadding = PaddingValues()
    )
}

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Error") },
        text = { Text(text = errorMessage) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    ErrorDialog(
        errorMessage = "Error message",
        onDismiss = { }
    )
}