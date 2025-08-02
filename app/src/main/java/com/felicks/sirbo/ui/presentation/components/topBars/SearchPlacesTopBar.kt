package com.felicks.sirbo.ui.presentation.components.topBars

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicks.sirbo.data.remote.photon.PhotonFeature
import com.felicks.sirbo.data.remote.photon.toCompactLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBarLegacy(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    isOrigin: Boolean,
    onBackPressed: () -> Unit,
    placeholder: String = "Buscar ubicación"
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
                placeholder = { Text(text = placeholder) },
                singleLine = true,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    isOrigin: Boolean,
    onBackPressed: () -> Unit,
    searchResults: List<PhotonFeature>,
    onResultClick: (PhotonFeature) -> Unit,
    placeholder: String = "Buscar ubicación"
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searchQuery,
            onQueryChange = {
                onSearchQueryChanged(it)
                expanded = true
            },
            onSearch = {
                expanded = false
                // Opcional: hacer algo más cuando se pulse enter o buscar explícitamente
            },
            active = expanded,
            onActiveChange = { expanded = it },
            placeholder = { Text(placeholder) },
            leadingIcon = {
                IconButton(onClick = {
                    if (expanded) {
                        expanded = false
                    } else {
                        onBackPressed()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        onSearchQueryChanged("")
                        expanded = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Borrar búsqueda"
                        )
                    }
                }
            }
        ) {
            // Aquí mostramos los resultados de búsqueda dentro del dropdown de SearchBar
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(searchResults) { result ->
                    ListItem(
                        headlineContent = { Text(result.properties.toCompactLabel()) },
                        modifier = Modifier
                            .clickable {
                                onResultClick(result)
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        onQueryChange(it)
                        expanded = true // abrir barra al cambiar texto
                    },
                    onSearch = {
                        onSearch(query)
                        expanded = false // cerrar al buscar
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
//                    placeholder = { Text("Buscar...") },
                    placeholder = { Text("Busca un dirección. Ej. Avenida Carrasco") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    trailingIcon = if (query.isNotEmpty()) {
                        {
                            IconButton(onClick = {
                                onQueryChange("")
                                expanded = false
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar")
                            }
                        }
                    } else null
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            LazyColumn {
                items(searchResults.size) { index ->
                    val result = searchResults[index]
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onResultClick(result)
                                expanded = false
                            }
                            .padding(vertical = 4.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimpleSearchBar() {
    var query by remember { mutableStateOf("") }
    val sampleResults = listOf(
        "Manzana",
        "Banana",
        "Cereza",
        "Durazno",
        "Frambuesa",
        "Kiwi",
        "Lima",
        "Melón",
        "Naranja",
        "Papaya",
        "Uva"
    ).filter { it.contains(query, ignoreCase = true) }

    SimpleSearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { /* Acción al buscar, p.ej. hacer algo con la query */ },
        searchResults = sampleResults,
        onResultClick = { selected ->
            query = selected
            // Acción cuando el usuario selecciona un resultado
        }
    )
}

