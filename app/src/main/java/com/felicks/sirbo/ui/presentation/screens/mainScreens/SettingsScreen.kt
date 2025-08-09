package com.felicks.sirbo.ui.presentation.screens.mainScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

val developers = listOf(
    Developer("FelicksDev", "Desarrollador Principal", email = "felicks@example.com", github = "github.com/felicksdev"),
    Developer("Otro Dev", "Diseñador UI/UX")
)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // Estados recolectados
    val isOnline by viewModel.isOnline.collectAsState()
    val isServerOnline by viewModel.isServerOnline.collectAsState()
    val lastSync by viewModel.lastSync.collectAsState()
    val darkModeEnabled by viewModel.darkMode.collectAsState()
    val isCheckingInternet by viewModel.isCheckingInternet.collectAsState()
    val isCheckingServer by viewModel.isCheckingServer.collectAsState()

    // Estados locales para notificaciones y privacidad
    val notificationsEnabled = remember { mutableStateOf(true) }
    val notificationSoundEnabled = remember { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.checkConnection()
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ConnectivitySection(
                isOnline = isOnline,
                isCheckingInternet = isCheckingInternet,
                isServerOnline = isServerOnline,
                isCheckingServer = isCheckingServer,
                onRetry = { viewModel.checkConnection() }
            )
            Spacer(Modifier.height(24.dp))

//            SynchronizationSection(
//                lastSync = lastSync,
//                onSyncNow = {
//                    viewModel.sincronizarManual()
//                    coroutineScope.launch {
//                        snackbarHostState.showSnackbar("Sincronización iniciada")
//                    }
//                }
//            )
//            Spacer(Modifier.height(24.dp))
//
//            AppearanceSection(
//                darkModeEnabled = darkModeEnabled,
//                onToggleDarkMode = { viewModel.toggleDarkMode() }
//            )
//            Spacer(Modifier.height(24.dp))

//            NotificationSection(
//                notificationsEnabled = notificationsEnabled.value,
//                onToggleNotifications = { notificationsEnabled.value = it },
//                notificationSoundEnabled = notificationSoundEnabled.value,
//                onToggleNotificationSound = { notificationSoundEnabled.value = it }
//            )
//            Spacer(Modifier.height(24.dp))

            AboutSection(
                version = "1.0.0",
                onLicensesClick = { /* Mostrar licencias */ },
                developers = developers
            )

            PrivacySecuritySection(
                onPrivacyPolicyClick = { /* Navegar a política de privacidad */ },
                onLogoutClick = { /* Lógica cerrar sesión */ }
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ConnectivitySection(
    isOnline: Boolean,
    isCheckingInternet: Boolean,
    isServerOnline: Boolean,
    isCheckingServer: Boolean,
    onRetry: () -> Unit
) {
    SectionTitle("Conectividad")

    ConnectivitySetting(
        title = "Conexión a Internet",
        connected = isOnline,
        isChecking = isCheckingInternet,
        onRetry = onRetry
    )
    ConnectivitySetting(
        title = "Estado del servidor",
        connected = isServerOnline,
        isChecking = isCheckingServer,
        onRetry = onRetry
    )
}

@Composable
private fun SynchronizationSection(
    lastSync: String?,
    onSyncNow: () -> Unit
) {
    SectionTitle("Sincronización")

    SettingItem(
        title = "Última sincronización",
        description = lastSync ?: "No sincronizado aún"
    )

    Button(
        onClick = onSyncNow,
//        modifier = Modifier.align(Alignment.End)
    ) {
        Text("Sincronizar ahora")
    }
}

@Composable
private fun AppearanceSection(
    darkModeEnabled: Boolean,
    onToggleDarkMode: () -> Unit
) {
    SectionTitle("Apariencia")

    SettingItem(
        title = "Modo oscuro",
        description = if (darkModeEnabled) "Activado" else "Desactivado",
        trailing = {
            Switch(
                checked = darkModeEnabled,
                onCheckedChange = { onToggleDarkMode() },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
            )
        }
    )
}

@Composable
private fun NotificationSection(
    notificationsEnabled: Boolean,
    onToggleNotifications: (Boolean) -> Unit,
    notificationSoundEnabled: Boolean,
    onToggleNotificationSound: (Boolean) -> Unit,
) {
    SectionTitle("Notificaciones")

    SettingItem(
        title = "Activar notificaciones",
        description = if (notificationsEnabled) "Activadas" else "Desactivadas",
        trailing = {
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = onToggleNotifications
            )
        }
    )

    SettingItem(
        title = "Sonido de notificación",
        description = if (notificationSoundEnabled) "Activado" else "Desactivado",
        trailing = {
            Switch(
                checked = notificationSoundEnabled,
                onCheckedChange = onToggleNotificationSound
            )
        }
    )
}

@Composable
private fun PrivacySecuritySection(
    onPrivacyPolicyClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
//    SectionTitle("Privacidad y Seguridad")
//
//    TextButton(onClick = onPrivacyPolicyClick) {
//        Text("Política de privacidad")
//    }
//    Spacer(Modifier.height(8.dp))
    Button(
        onClick = onLogoutClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Cerrar sesión", color = MaterialTheme.colorScheme.onError)
    }
}

@Composable
fun AboutSection(
    version: String,
    developers: List<Developer>,
    onLicensesClick: () -> Unit
) {
    val openDevDialog = remember { mutableStateOf(false) }

    SectionTitle("Acerca de")

    SettingItem(
        title = "Versión",
        description = version
    )
//    TextButton(onClick = onLicensesClick) {
//        Text("Licencias y créditos")
//    }
//    Spacer(Modifier.height(8.dp))

    SettingItem(
        title = "Desarrolladores",
        description = "${developers.size} personas",
        trailing = {
            TextButton(onClick = { openDevDialog.value = true }) {
                Text("Ver detalles")
            }
        }
    )

    if (openDevDialog.value) {
        DevelopersDialog(developers = developers, onDismiss = { openDevDialog.value = false })
    }
}

data class Developer(
    val name: String,
    val role: String,
    val email: String? = null,
    val github: String? = null
)

@Composable
fun DevelopersDialog(developers: List<Developer>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Desarrolladores") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                developers.forEach { dev ->
                    Text(text = dev.name, style = MaterialTheme.typography.bodyLarge)
                    Text(text = dev.role, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    dev.email?.let { Text(text = "Email: $it", style = MaterialTheme.typography.bodySmall) }
                    dev.github?.let { Text(text = "GitHub: $it", style = MaterialTheme.typography.bodySmall) }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ConnectivitySetting(
    title: String,
    connected: Boolean,
    isChecking: Boolean,
    onRetry: () -> Unit
) {
    SettingItem(
        title = title,
        description = if (connected) "Conectado" else "Sin conexión",
        trailing = {
            if (isChecking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    imageVector = if (connected) Icons.Default.Wifi else Icons.Default.WifiOff,
                    contentDescription = if (connected) "$title conectado" else "$title desconectado",
                    tint = if (connected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable { onRetry() }
                )
            }
        }
    )
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    trailing: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (trailing != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailing()
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
    }
}
