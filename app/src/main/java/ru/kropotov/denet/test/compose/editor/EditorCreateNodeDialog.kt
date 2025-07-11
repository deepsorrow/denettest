package ru.kropotov.denet.test.compose.editor

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kropotov.denet.test.R
import ru.kropotov.denet.test.ui.theme.buttonLarge
import ru.kropotov.denet.test.ui.theme.buttonShape

@Composable
fun EditorCreateNodeDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    var address by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = generateRandomHexString()
            )
        )
    }

    AlertDialog(
        containerColor = Color(0xFF1A1D25),
        text = {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.editor_create_label)) },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
                ),
                textStyle = TextStyle(fontSize = 30.sp)
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            DialogButton(
                text = stringResource(R.string.editor_create_confirm_button),
                onClick = { onConfirmation(address.text) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        dismissButton = {
            DialogButton(
                text = stringResource(R.string.button_back),
                onClick = { onDismissRequest() },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    )
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    Button(
        onClick = onClick,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Text(
            text = text,
            style = buttonLarge,
            color = contentColor
        )
    }
}

fun generateRandomHexString(): String {
    val hexChars = "0123456789abcdef"
    val random = java.util.Random()

    val sb = StringBuilder("0x")
    repeat(38) {
        val randomChar = hexChars[random.nextInt(hexChars.length)]
        sb.append(randomChar)
    }
    return sb.toString()
}

@Preview
@Composable
fun EditorCreateNodeDialogPreview() {
    EditorCreateNodeDialog({}, {})
}