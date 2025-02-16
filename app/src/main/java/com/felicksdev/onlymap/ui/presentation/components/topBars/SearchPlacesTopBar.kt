package com.felicksdev.onlymap.ui.presentation.components.topBars

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    isOrigin: Boolean,
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        title = {
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Buscar ubicación") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Borrar búsqueda"
                            )
                        }
                    }
                },
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                Log.d("SearchTopBar", "Back pressed")
                onBackPressed()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchTopBar() {
    SearchTopBar(
        searchQuery = "Avenida Politécnico",
        onSearchQueryChanged = {},
        isOrigin = true,
        onBackPressed = {}
    )
}


