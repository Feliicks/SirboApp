import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RouteSelectionScreen(onNextClick: (String, String) -> Unit) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = origin,
            onValueChange = { origin = it },
            label = { Text("Origen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destino") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (origin.isNotEmpty() && destination.isNotEmpty()) {
                    onNextClick(origin, destination)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Siguiente")
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun SelectRouteScreenPreview() {
    RouteSelectionScreen(onNextClick = { _, _ -> })
}
