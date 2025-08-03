package com.felicks.sirbo.domain.models

enum class SyncStatus {
    MOSTRANDO_LOCAL,
    SINCRONIZANDO,
    ERROR_INSERCION,
    ERROR_CONEXION,
    ERROR_GENERAL,
    VACIO_REMOTO,
    COMPLETADO,
    SINCRONIZADO, // ← o COMPLETADO_EXITO
}

