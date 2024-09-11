@file:OptIn(ExperimentalMaterial3Api::class)

package com.felicksdev.onlymap.presentation.screens.main


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.plus
import com.felicksdev.onlymap.presentation.components.LoadingScreen
import com.felicksdev.onlymap.presentation.components.MyMap
import com.felicksdev.onlymap.presentation.components.bottomBars.SheetContent
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

    LaunchedEffect(Unit) {
//        si plan es definido y vvalido entonces cargar una loading screen hazstal respuesta de fetchplan
        if (plannerViewModel.isPlacesDefined()) {
            plannerViewModel.fetchPlan()
        }
    }
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
    ) { padding ->
        val fullPading = bottomPadding.plus(padding)
        HomeScreenContent(
            padding = fullPading,
            cameraPositionState = cameraState,
            plannerViewModel = plannerViewModel
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
    cameraPositionState: CameraPositionState,
    plannerViewModel: PlannerViewModel
) {
    val planResult by plannerViewModel.planResult.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 40.dp, // Altura mínima visible del BottomSheet
        sheetContent = {
            // Contenido del BottomSheet
            Column(
                modifier = Modifier

                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Aquí colocas el contenido del BottomSheet
                planResult?.let { plan ->
                    SheetContent(itineraries = plan.itineraries)
                }
            }
        },
        content = { innerPadding ->
            // Contenido principal de la pantalla

            MyMap(
                cameraPositionState = cameraPositionState,
                padding = innerPadding,
                itinerary = planResult?.itineraries?.get(0)
            )

        }
    )

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