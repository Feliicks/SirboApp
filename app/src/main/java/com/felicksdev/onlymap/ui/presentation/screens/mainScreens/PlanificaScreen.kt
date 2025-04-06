@file:OptIn(ExperimentalMaterial3Api::class)

package com.felicksdev.onlymap.ui.presentation.screens.mainScreens


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.data.models.otpModels.routing.Itinerary
import com.felicksdev.onlymap.data.models.otpModels.routing.Plan
import com.felicksdev.onlymap.isSetted
import com.felicksdev.onlymap.toLatLng
import com.felicksdev.onlymap.ui.common.SheetValue
import com.felicksdev.onlymap.ui.navigation.plus
import com.felicksdev.onlymap.ui.presentation.components.LoadingScreen
import com.felicksdev.onlymap.ui.presentation.components.MyMap
import com.felicksdev.onlymap.ui.presentation.components.bottomBars.SheetContent
import com.felicksdev.onlymap.ui.presentation.components.topBars.SmallRouterPlannerBar
import com.felicksdev.onlymap.ui.presentation.dialogs.OtpConfigDialog
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import io.morfly.compose.bottomsheet.material3.layoutHeightDp
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState
import io.morfly.compose.bottomsheet.material3.requireSheetVisibleHeightDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanificaScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController,
    myCameraPositionState: CameraPositionState,
    plannerViewModel: PlannerViewModel,
    navBarPadding: PaddingValues,
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

    var showConfigDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val config by plannerViewModel.config.collectAsState()

    LaunchedEffect(Unit) {
        plannerViewModel.loadConfig(context)
    }

    Scaffold(
        topBar = {
            SmallRouterPlannerBar(
                plannerViewModel = plannerViewModel,
                navController = navController,
                onMenuClick = {
                    showConfigDialog = true
                }
            )
        },
    ) { padding ->
        PlanificaScreenContent(
            bottomLayoutPadding = navBarPadding,
            topPadding = padding,
            cameraPositionState = cameraState,
            plannerViewModel = plannerViewModel
        )

        if (isLoading) LoadingScreen()

        // Mostrar AlertDialog si hay un error
        errorState?.let { errorMessage ->
            ErrorDialog(
                errorMessage = errorMessage,
                onDismiss = { plannerViewModel.clearError() }
            )
        }
        if (showConfigDialog) {
            Log.d("OtpDialog", "Mostrando el dialog de configuración OTP")

            OtpConfigDialog(
                initialConfig = config,
                onDismiss = { showConfigDialog = false },
                onSave = {
                    plannerViewModel.saveConfig(context = context, config = it)
                    showConfigDialog = false
                },
                onReset = {
                    plannerViewModel.resetConfig(context)
//                    showConfigDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanificaScreenContent(
    bottomLayoutPadding: PaddingValues,
    topPadding: PaddingValues,
    cameraPositionState: CameraPositionState,
    plannerViewModel: PlannerViewModel
) {
    val planResult by plannerViewModel.planResult.collectAsState()

    val isPlacesDefined by plannerViewModel.isLocationDefined.collectAsState()

    val itinerarySelected by plannerViewModel.selectedItinerary.collectAsState()

    var isInitialState by rememberSaveable { mutableStateOf(true) }

    val fromLocation by plannerViewModel.fromLocation.collectAsState()
    val toLocation by plannerViewModel.toLocation.collectAsState()


    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        defineValues = {
            // Bottom sheet height is 100 dp.
            SheetValue.Collapsed at height(100.dp)
            if (isInitialState) {
                // Offset is 60% which means the bottom sheet takes 40% of the screen.
                SheetValue.PartiallyExpanded at offset(percent = 60)
            }
            // Bottom sheet height is equal to the height of its content.
            SheetValue.Expanded at contentHeight
        },
        confirmValueChange = {
            if (isInitialState) {
                isInitialState = false
                refreshValues()
            }
            true
        }
    )
    val scaffoldState =
        io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState(sheetState)

    io.morfly.compose.bottomsheet.material3.BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            planResult?.let {
                BottomSheetContent(it, onItinerarySelected = { index ->
                    plannerViewModel.setItinerarySelected(index)
                })
            }
        }
    ) { innerPadding ->
        val bottomPadding by remember {
            derivedStateOf { sheetState.requireSheetVisibleHeightDp() }
        }
        MyMap(
            cameraPositionState = cameraPositionState,
            padding = if (isPlacesDefined) topPadding else topPadding.plus(bottomLayoutPadding),
            bottomPadding = bottomPadding,
            itinerarySelected = itinerarySelected,
            listItinerary = planResult?.itineraries ?: emptyList(),
            layoutHeight = sheetState.layoutHeightDp,
            markers = {
                if (fromLocation.isSetted()) {
                    // Dibujar marcador de origen
                    Marker(
                        state = MarkerState(
                            position = fromLocation.toLatLng(),
                        ),
                        title = "Origen"
                    )
                }
                if (toLocation.isSetted()) {
                    // Dibujar marcador de origen
                    Marker(
                        state = MarkerState(
                            position = toLocation.toLatLng(),
                        ),
                        title = "Origen"
                    )
                }

            }
        )
    }
}

@Composable
private fun BottomSheetContent(planResult: Plan, onItinerarySelected: (Itinerary) -> Unit) {
    SheetContent(listItineraries = planResult.itineraries, onItinerarySelectedIndex = { it ->
        Log.d("HomeScreen", "Itinerary selected: $it")
        onItinerarySelected(it)
    })
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
    PlanificaScreen(
        viewModel = HomeScreenViewModel(), // Instancia de ViewModel de prueba
        navController = navController,
        myCameraPositionState = CameraPositionState(),
        plannerViewModel = hiltViewModel(),
        navBarPadding = PaddingValues(),
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