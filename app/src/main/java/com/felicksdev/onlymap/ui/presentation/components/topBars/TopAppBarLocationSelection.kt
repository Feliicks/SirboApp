package com.felicksdev.onlymap.ui.presentation.components.topBars

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionTopAppBar() {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "Medium Top App Bar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = "",
                    onValueChange = {}, // Implementar lógica para actualizar valor
                    label = { Text("Origen") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = "",
                    onValueChange = {}, // Implementar lógica para actualizar valor
                    label = { Text("Destino") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        },
    )
}

@Preview
@Composable
fun TopBarPreview() {
    LocationSelectionTopAppBar()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar() {
    var location1 by remember { mutableStateOf("") }
    var location2 by remember { mutableStateOf("") }

    LargeTopAppBar(
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = "",
                    onValueChange = {}, // Implementar lógica para actualizar valor
                    label = { Text("Origen") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = "",
                    onValueChange = {}, // Implementar lógica para actualizar valor
                    label = { Text("Destino") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },

        )

}

@Preview
@Composable
fun PreviewCustomTopBar(modifier: Modifier = Modifier) {
    CustomTopBar()
}

@Preview
@Composable
fun ScreenPreview() {
    Scaffold(
        topBar = {
            CustomTopBar()
        }
    ) { innerPadding ->
        Text(
            text = "Contenido de la pantalla",
            modifier = Modifier.padding(innerPadding)
        )

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar() {
    var searchText by rememberSaveable { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back button click */ }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicTextField(
                    value = searchText,
                    singleLine = true,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(Color.White),
                    onValueChange = { newText -> searchText = newText }
                ) { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = searchText,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        interactionSource = interactionSource,
                        visualTransformation = VisualTransformation.None,
                        placeholder = {
                            Text(
                                text = "Hola"
                            )
                        },
                        container = {
                            OutlinedTextFieldDefaults.ContainerBox(
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = OutlinedTextFieldDefaults.colors(),
                                shape = RoundedCornerShape(10.dp),
                                focusedBorderThickness = 2.dp,
                                unfocusedBorderThickness = 1.dp,

                            )
                        }
                    )
                }

                OutlinedTextField(

                    value = "Choose your destination",
                    onValueChange = {
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    textStyle = LocalTextStyle.current.copy(color = Color.Gray),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /* Handle more options click */ }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More options",
                    tint = Color.Black)
            }
        }
    }
}
@Preview
@Composable
fun CustomTopAppBarSample() {
    CustomTopAppBar()
}











