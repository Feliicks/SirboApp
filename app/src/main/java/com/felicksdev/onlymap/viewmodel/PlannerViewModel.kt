package com.felicksdev.onlymap.viewmodel


import androidx.lifecycle.ViewModel
import com.felicksdev.onlymap.TrufiLocation
import com.felicksdev.onlymap.data.models.RoutePlanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlannerViewModel : ViewModel() {
    private val _plannerState = MutableStateFlow(RoutePlanner())
    val plannerState: StateFlow<RoutePlanner> = _plannerState.asStateFlow()

    fun setFromPlace(fromPlace: TrufiLocation) {
        _plannerState.value = _plannerState.value.copy(fromPlace = fromPlace)
        // Aquí puedes guardar el estado si es necesario
    }

    fun setToPlace(toPlace: TrufiLocation) {
        _plannerState.value = _plannerState.value.copy(toPlace = toPlace)
        // Aquí puedes guardar el estado si es necesario
    }

    fun swapLocations() {
        _plannerState.value = _plannerState.value.copy(
            fromPlace = _plannerState.value.toPlace,
            toPlace = _plannerState.value.fromPlace
        )

        // Aquí puedes guardar el estado si es necesario
    }

    fun reset() {
        _plannerState.update { currentState ->
            currentState.copy(
                fromPlace = null,
                toPlace = null
            )
        }
        // Aquí puedes guardar el estado si es necesario
    }

    fun fetchPlan() {
        // Implementa la lógica para fetchPlan aquí
    }


    fun isPlacesDefined() = _plannerState.value.isPlacesDefined
    fun testSetLocations (){
        _plannerState.value = RoutePlanner(
            fromPlace = TrufiLocation(
                description = "Origen",
                latitude = -17.7833,
                longitude = -63.1667
            ),
        )
    }
}