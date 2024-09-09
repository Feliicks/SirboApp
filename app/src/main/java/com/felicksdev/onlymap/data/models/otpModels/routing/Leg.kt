package com.felicksdev.onlymap.data.models.otpModels.routing

data class Leg(
    val agencyTimeZoneOffset: Int,
    val arrivalDelay: Int,
    val departureDelay: Int,
    val distance: Double,
    val duration: Double,
    val endTime: Long,
    val flexDrtAdvanceBookMin: Double,
    val from: FromLeg,
    val interlineWithPreviousLeg: Boolean,
    val legGeometry: LegGeometry,
    val routeShortName: String? = null,
    val routeLongName: String? = null,
    val mode: String,
    val pathway: Boolean,
    val realTime: Boolean,
    val rentedBike: Boolean,
    val route: String,
    val startTime: Long,
    val steps: List<Step>,
    val to: ToLeg,
    val transitLeg: Boolean
)