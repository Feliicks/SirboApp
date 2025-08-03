package com.felicks.sirbo.ui.presentation.screens.mainScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.felicks.sirbo.domain.models.User
import com.felicks.sirbo.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val user by userViewModel.user.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil") }
            )
        }
    ) { paddingValues ->
        ProfileScreenContent(
            user = user,
            paddingValues = paddingValues,
            onNavigateToSettings = { navController.navigate("settings_screen") },
            onSignOut = { userViewModel.signOut() },
            onLogin = { /* TODO: Acción de login */ },
            onChangePassword = { /* TODO: Acción de cambiar contraseña */ },
            onSupport = { /* TODO: Acción de soporte */ }
        )
    }
}

@Composable
fun ProfileScreenContent(
    user: User?,
    paddingValues: PaddingValues,
    onNavigateToSettings: () -> Unit,
    onSignOut: () -> Unit,
    onLogin: () -> Unit,
    onChangePassword: () -> Unit,
    onSupport: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
//            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(24.dp))

        val photoUrl = user?.photoUrl?.takeIf { it.isNotBlank() }

        if (photoUrl != null) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = user?.username ?: "Invitado",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "@${user?.code}",
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )

        user?.email?.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(32.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))

        Column(
//            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            ProfileOption(Icons.Default.Settings, "Configuración", onNavigateToSettings)
            ProfileOption(Icons.Default.Lock, "Cambiar contraseña", onChangePassword)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp))
            ProfileOption(Icons.Default.Help, "Ayuda y soporte", onSupport)

            if (user?.isGuest == true) {
                // Usuario invitado (guest) → mostrar "Iniciar sesión"
                ProfileOption(Icons.Default.Login, "Iniciar sesión", onLogin)
            } else {
                // Usuario registrado o nulo → mostrar "Cerrar sesión"
                ProfileOption(Icons.Default.Logout, "Cerrar sesión", onSignOut)
            }

        }
    }
}

@Composable
fun ProfileOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 26.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f) // Ocupa el máximo espacio a la izquierda
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}




