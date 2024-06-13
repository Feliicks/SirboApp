import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.data.models.AddressState

@Composable
fun LocationField(
    locationState: AddressState,
    locationAddress: String, onFieldSelected: () -> Unit, label: String,
    focusRequester: FocusRequester? = null
) {
    OutlinedTextField(
        value = locationState.address,
        onValueChange = { },
        label = { Text(label) },
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFieldSelected()
                }
            }
            .padding(bottom = 16.dp)
            .focusRequester(focusRequester ?: FocusRequester()),

        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
//                            onFieldSelected()
                        }
                    }
                }
            },
    )
}