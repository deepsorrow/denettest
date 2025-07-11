package ru.kropotov.denet.test.compose.node

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.kropotov.denet.test.compose.common.BottomSheetSelect
import ru.kropotov.denet.test.data.node.Node
import ru.kropotov.denet.test.ui.theme.buttonMedium
import ru.kropotov.denet.test.ui.theme.buttonShape

@Composable
fun NodeNextBottomSheet(
    node: Node,
    onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    BottomSheetSelect(
        items = node.children,
        onDismiss = { onDismiss() },
        itemContent = { child ->
            NodeNextItem(
                address = child,
                onItemSelected = {
                    onItemSelected(it).also { onDismiss() }
                }
            )
        }
    )
}

@Composable
fun NodeNextItem(
    address: String,
    onItemSelected: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemSelected(address) }
        .background(color = Color(0xFF2A2F3A), shape = buttonShape)
        .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AbstractNodeShape(seed = address, size = 36.dp, isMinimized = true)

        Spacer(Modifier.width(8.dp))

        Text(
            text = address,
            color = Color.White,
            style = buttonMedium,
            overflow = TextOverflow.MiddleEllipsis,
            maxLines = 1
        )
    }
}