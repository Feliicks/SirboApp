package com.felicks.sirbo.core

object OtpEndpoints {
    private const val PREFIX = "sirbo/api/"

    const val PING = PREFIX
    const val INDEX_ROUTES = "${PREFIX}plan/rutas"
    const val GET_ROUTE_STOPS = "${PREFIX}plan/rutas/{id}/stops"
    const val GET_PATTERN_BY_ROUTE_ID = "${PREFIX}plan/rutas/{routeId}/patterns"
    const val GET_PATTERN_DETAILS_BY_ID = "${PREFIX}plan/patterns/{patternId}"
    const val FETCH_PLAN = "${PREFIX}plan"
    const val GET_ROUTE_DETAIL = "${PREFIX}plan/rutas/{id}"
    const val GET_OPTIMAL_ROUTES = "${PREFIX}otp/routers/default/plan"
    const val GET_ROUTE_PATTERNS = "${PREFIX}otp/routers/default/index/patterns/{id}"
    const val GET_GEOM_BY_PATTERN = "${PREFIX}plan/patterns/{patternId}/geometry"
}
