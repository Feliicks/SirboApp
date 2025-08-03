package com.felicks.sirbo.data.models

enum class SyncStatus {
    MOSTRANDO_LOCAL,
    SINCRONIZANDO,
    ERROR_INSERCION,
    ERROR_CONEXION,
    ERROR_GENERAL,
    VACIO_REMOTO,
    SINCRONIZADO, // ← o COMPLETADO_EXITO
}

