package com.felicksdev.onlymap.data.models.otpModels.routing

data class Itinerary(
    val duration: Int,
    val elevationGained: Double,
    val elevationLost: Double,
    val endTime: Long,
    val legs: List<Leg>,
    val startTime: Long,
    val tooSloped: Boolean,
    val transfers: Int,
    val transitTime: Int,
    val waitingTime: Int,
    val walkDistance: Double,
    val walkLimitExceeded: Boolean,
    val walkTime: Int
){
    fun getDurationInMinutes(): Int {
        return duration / 60
    }
}

val sampleItineraries = listOf(
    Itinerary(
        duration = 584,
        startTime = 1725817851000,
        endTime = 1725818435000,
        walkTime = 449,
        transitTime = 133,
        waitingTime = 2,
        walkDistance = 564.7839097707605,
        walkLimitExceeded = false,
        elevationLost = 0.0,
        elevationGained = 0.0,
        transfers = 0,
        tooSloped = false,
        legs = listOf(
            Leg(
                startTime = 1725817851000,
                endTime = 1725817866000,
                departureDelay = 0,
                arrivalDelay = 0,
                realTime = false,
                distance = 18.939,
                pathway = false,
                mode = "WALK",
                route = "",
                agencyTimeZoneOffset = -14400000,
                interlineWithPreviousLeg = false,
                from = FromLeg(
                    name = "Origin",
                    lon = -68.1508,
                    lat = -16.49561,
                    vertexType = "NORMAL",
                    orig = ""
                ),
                to = ToLeg(
                    name = "Avenida Mariano Baptista",
                    lon = -68.1506229,
                    lat = -16.4956231,
                    departure = 1725817867000,
                    arrival = 1725817866000,
                    stopId = "1:3891187112",
                    stopIndex = 7,
                    stopSequence = 7,
                    vertexType = "TRANSIT",
                    boardAlightType = "DEFAULT"
                ),
                legGeometry = LegGeometry(
                    points = "pxtcBnu}~KBa@??",
                    length = 3
                ),
                rentedBike = false,
                flexDrtAdvanceBookMin = 0.0,
                duration = 15.0,
                transitLeg = false,
                steps = listOf(
                    Step(
                        distance = 18.939,
                        relativeDirection = "DEPART",
                        streetName = "Avenida Mariano Baptista",
                        absoluteDirection = "EAST",
                        stayOn = false,
                        area = false,
                        bogusName = false,
                        lon = -68.15079997945745,
                        lat = -16.49560974777823,
                        elevation = emptyList()
                    )
                )
            ),
            Leg(
                startTime = 1725817851000,
                endTime = 1725817866000,
                departureDelay = 0,
                arrivalDelay = 0,
                realTime = false,
                distance = 18.939,
                pathway = false,
                mode = "BUS",
                routeLongName = "Minibus 893: Reyesano -> Rio Abuná",
                routeShortName = "893",
                route = "",
                agencyTimeZoneOffset = -14400000,
                interlineWithPreviousLeg = false,
                from = FromLeg(
                    name = "Origin",
                    lon = -68.1508,
                    lat = -16.49561,
                    vertexType = "NORMAL",
                    orig = ""
                ),
                to = ToLeg(
                    name = "Avenida Mariano Baptista",
                    lon = -68.1506229,
                    lat = -16.4956231,
                    departure = 1725817867000,
                    arrival = 1725817866000,
                    stopId = "1:3891187112",
                    stopIndex = 7,
                    stopSequence = 7,
                    vertexType = "TRANSIT",
                    boardAlightType = "DEFAULT"
                ),
                legGeometry = LegGeometry(
                    points = "pxtcBnu}~KBa@??",
                    length = 3
                ),
                rentedBike = false,
                flexDrtAdvanceBookMin = 0.0,
                duration = 15.0,
                transitLeg = false,
                steps = listOf(
                    Step(
                        distance = 18.939,
                        relativeDirection = "DEPART",
                        streetName = "Avenida Mariano Baptista",
                        absoluteDirection = "EAST",
                        stayOn = false,
                        area = false,
                        bogusName = false,
                        lon = -68.15079997945745,
                        lat = -16.49560974777823,
                        elevation = emptyList()
                    )
                )
            ),
            Leg(
                startTime = 1725817851000,
                endTime = 1725817866000,
                departureDelay = 0,
                arrivalDelay = 0,
                realTime = false,
                distance = 18.939,
                pathway = false,
                mode = "WALK",
                route = "",
                agencyTimeZoneOffset = -14400000,
                interlineWithPreviousLeg = false,
                from = FromLeg(
                    name = "Origin",
                    lon = -68.1508,
                    lat = -16.49561,
                    vertexType = "NORMAL",
                    orig = ""
                ),
                to = ToLeg(
                    name = "Avenida Mariano Baptista",
                    lon = -68.1506229,
                    lat = -16.4956231,
                    departure = 1725817867000,
                    arrival = 1725817866000,
                    stopId = "1:3891187112",
                    stopIndex = 7,
                    stopSequence = 7,
                    vertexType = "TRANSIT",
                    boardAlightType = "DEFAULT"
                ),
                legGeometry = LegGeometry(
                    points = "pxtcBnu}~KBa@??",
                    length = 3
                ),
                rentedBike = false,
                flexDrtAdvanceBookMin = 0.0,
                duration = 15.0,
                transitLeg = false,
                steps = listOf(
                    Step(
                        distance = 18.939,
                        relativeDirection = "DEPART",
                        streetName = "Avenida Mariano Baptista",
                        absoluteDirection = "EAST",
                        stayOn = false,
                        area = false,
                        bogusName = false,
                        lon = -68.15079997945745,
                        lat = -16.49560974777823,
                        elevation = emptyList()
                    )
                )
            ),
            // Similar structure for other legs...
        )
    ),
    // Another itinerary...
)