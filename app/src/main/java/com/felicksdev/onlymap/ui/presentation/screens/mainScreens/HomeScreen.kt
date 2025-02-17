@file:OptIn(ExperimentalMaterial3Api::class)

package com.felicksdev.onlymap.ui.presentation.screens.mainScreens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.ui.navigation.plus
import com.felicksdev.onlymap.ui.presentation.components.LoadingScreen
import com.felicksdev.onlymap.ui.presentation.components.MyMap
import com.felicksdev.onlymap.ui.presentation.components.bottomBars.SheetContent
import com.felicksdev.onlymap.ui.presentation.components.topBars.SmallRouterPlannerBar
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
    bottomPadding: PaddingValues,
) {

    val isPlacesDefined by plannerViewModel.isLocationDefined.collectAsState()

    LaunchedEffect(isPlacesDefined) {
        if (isPlacesDefined) {
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
            planResult?.let { plan -> SheetContent(itineraries = plan.itineraries) }
        }
    ) { innerPadding ->
        MyMap(
            cameraPositionState = cameraPositionState,
            padding = if (plannerViewModel.isPlacesDefined()) innerPadding else padding,
            itinerary = planResult?.itineraries?.getOrNull(0)
        )
    }
}
@Composable
fun LoadingScreen(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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
        plannerViewModel = hiltViewModel(),
        bottomPadding = PaddingValues(),
    )
}

@Composable
fun ErrorDialog(errorMessage: String?, onDismiss: () -> Unit) {
    errorMessage?.let {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = onDismiss) { Text("Aceptar") }
            },
            text = { Text(it) }
        )
    }
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    ErrorDialog(
        errorMessage = "Error message",
        onDismiss = { }
    )
}