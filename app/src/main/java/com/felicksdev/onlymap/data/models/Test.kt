package com.felicksdev.onlymap.data.models

import RutasViewModel
import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem
import com.felicksdev.onlymap.data.models.otpModels.routing.FromLeg
import com.felicksdev.onlymap.data.models.otpModels.routing.FromX
import com.felicksdev.onlymap.data.models.otpModels.routing.Leg
import com.felicksdev.onlymap.data.models.otpModels.routing.LegGeometry
import com.felicksdev.onlymap.data.models.otpModels.routing.Step
import com.felicksdev.onlymap.data.models.otpModels.routing.ToLeg
import com.felicksdev.onlymap.data.models.otpModels.routing.ToX
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.LatLng

val rutaTest = RoutesModelItem(
    id = "893",
    agencyName = "default",
    longName = "MiniBus 893: Reyesano → Rio Abuná",
    mode = "BUS",
    shortName = "893"
)

var testSteps = emptyList<Step>()
var fromTest = FromX(
    departure = 0,
    lat = -16.5006,
    lon = -68.1336,
    name = "Origin",
    orig = "Origin",
    vertexType = "NORMAL"
)
var toTest = ToX(
    lat = -16.5006,
    lon = -68.1336,
    name = "Origin",
    orig = "Origin",
    vertexType = "NORMAL"
)
val sampleLegs = listOf(
    Leg(
        startTime = 1718199239000,
        endTime = 1718199268000,
        departureDelay = 0,
        arrivalDelay = 0,
        realTime = false,
        distance = 33.841,
        pathway = false,
        mode = "WALK",
        route = "",
        agencyTimeZoneOffset = -14400000,
        interlineWithPreviousLeg = false,
        from = FromLeg(
            name = "Origin", lon = -68.132, lat = -16.50024,
            orig = "",
            vertexType = ""
        ),
        to = ToLeg(
            name = "Avenida Eliodoro Camacho",
            lon = -68.1319705,
            lat = -16.4999285,
            arrival = 153,
            vertexType = "",
            departure = 0,
            stopId = "",
            stopIndex = 0,
            stopSequence = 0,
            boardAlightType = "DEFAULT"
        ),
        legGeometry = LegGeometry(points = "juucBl`z~Kc@OK?IA", length = 4),
        rentedBike = false,
        flexDrtAdvanceBookMin = 0.0,
        duration = 29.0,
        transitLeg = false,

        steps = emptyList()
    ),
    Leg(
        startTime = 1718199269000,
        endTime = 1718199337000,
        departureDelay = 0,
        arrivalDelay = 0,
        realTime = false,
        distance = 906.0317730679567,
        pathway = false,
        mode = "BUS",
        route = "893",
        agencyTimeZoneOffset = -14400000,
        interlineWithPreviousLeg = false,
        from = FromLeg(
            name = "Origin", lon = -68.132, lat = -16.50024,
            orig = "",
            vertexType = ""
        ),
        to = ToLeg(
            name = "Avenida Eliodoro Camacho",
            lon = -68.1319705,
            lat = -16.4999285,
            arrival = 153,
            vertexType = "",
            departure = 0,
            stopId = "",
            stopIndex = 0,
            stopSequence = 0,
            boardAlightType = "DEFAULT"
        ),
        legGeometry = LegGeometry(
            points = "psucBz_z~Kr@gBn@cBn@mAHSBYAWAMEMMc@Yk@Wk@I[Ga@E[Ca@Cs@@U@IBSVw@Vy@L_@Ji@Jo@F]AUCWOm@_@eAWi@e@iAg@qAQo@EQMc@AQAG@WJ{@",
            length = 40
        ),
        routeLongName = "",
        routeShortName = "",
        rentedBike = false,
        flexDrtAdvanceBookMin = 0.0,
        duration = 68.0,
        transitLeg = true,
        steps = emptyList()
    )
)

fun LocationViewModel.setTestData() {
    // Simula una ubicación de origen y destino para la vista previa
    originLocationState.value = AddressState(
        address = "Origen de prueba",
        coordinates = LatLng(37.7749, -122.4194) // Ejemplo: San Francisco
    )
    destinationLocationState.value = AddressState(
        address = "Destino de prueba",
        coordinates = LatLng(34.0522, -118.2437) // Ejemplo: Los Ángeles
    )
}

fun RutasViewModel.setTestRoutes() {
    // Simula datos de rutas óptimas para la vista previa
//    optimalRouteLegs = com.felicksdev.onlymap.data.models.sampleLegs


}
//val legs = listOf(
//    Leg(
//        mode = "WALK",
//        distance = 33.841,
//        route = "",
//        from = fromTest,
//        to = "Avenida Eliodoro Camacho",
//        duration = 29.0,
//        steps = toTest
//    ),
//    Leg(
//        mode = "BUS",
//        distance = 906.0317730679567,
//        route = "893",
//        agencyName = "default",
//        agencyUrl = "https://www.example.com/",
//        routeType = 3,
//        routeId = "1:17383390",
//        tripId = "1:3",
//        serviceDate = "20240612",
//        from = "Avenida Eliodoro Camacho",
//        to = "Calle Capitán Hugo Estrada",
//        duration = 68.0,
//        routeShortName = "893",
//        routeLongName = "MiniBus 893: Reyesano → Rio Abuná",
//        steps = []
//    ),
//    Leg(
//        mode = "WALK",
//        distance = 251.651,
//        route = "",
//        from = "Calle Capitán Hugo Estrada",
//        to = "Destination",
//        duration = 223.0,
//        steps = listOf(
//            Step(
//                distance = 16.618,
//                streetName = "Calle Capitán Hugo Estrada",
//                absoluteDirection = "EAST",
//                stayOn = false,
//                area = false,
//                bogusName = false
//            ),
//            Step(
//                distance = 14.556,
//                streetName = "path",
//                absoluteDirection = "NORTH",
//                stayOn = false,
//                area = false,
//                bogusName = true
//            ),
//            Step(
//                distance = 10.745,
//                streetName = "path",
//                absoluteDirection = "SOUTH",
//                stayOn = true,
//                area = false,
//                bogusName = true
//            ),
//            Step(
//                distance = 209.732,
//                streetName = "Avenida Bautista Saavedra",
//                absoluteDirection = "NORTHWEST",
//                stayOn = false,
//                area = false,
//                bogusName = false
//            )
//        )
//    )
//)