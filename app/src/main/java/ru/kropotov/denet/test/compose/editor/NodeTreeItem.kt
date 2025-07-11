package ru.kropotov.denet.test.compose.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kropotov.denet.test.compose.node.AbstractNodeShape
import ru.kropotov.denet.test.ui.theme.buttonShape
import ru.kropotov.denet.test.ui.theme.buttonSmall
import ru.kropotov.denet.test.viewmodel.EditorViewModel

@Composable
fun NodeTreeItem(
    viewModel: EditorViewModel = hiltViewModel(),

    node: NodeUiModel,
    onToggle: (NodeUiModel) -> Unit,
    level: Int, isCurrent: Boolean
) {
    val currentNodeParents = viewModel.pathToCurrentNode.collectAsStateWithLifecycle().value
    val isCurrentNodeParent = node.address in currentNodeParents

    val bgColor = when {
        isCurrent -> Color(0xFFFFD600)
        isCurrentNodeParent -> Color(0xFF343C52)
        else -> Color(0xFF2A2F3A)
    }
    val textColor = if (isCurrent ) Color.Black else Color(0xFFEAEAEA)

    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Row(Modifier.padding(start = (level * 24).dp, top = 4.dp, bottom = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor, shape = buttonShape)
                .clickable {
                    isBottomSheetVisible = true
                }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            TextButton(
                modifier = Modifier.size(24.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = { onToggle(node) }
            ) {
                if (node.children.isNotEmpty()) {
                    Text(
                        text = if (node.isExpanded) "▼" else "▶",
                        color = textColor
                    )
                }
            }

            AbstractNodeShape(seed = node.address, size = 20.dp, isMinimized = true)

            Spacer(Modifier.width(4.dp))

            Text(
                text = node.address,
                color = textColor,
                style = buttonSmall,
                overflow = TextOverflow.MiddleEllipsis,
                maxLines = 1
            )
        }
    }

    if (isBottomSheetVisible) {
        EditorBottomSheet(node = node, onDismiss = { isBottomSheetVisible = false })
    }
}

@Preview
@Composable
fun NodeTreeItemPreview() {
    NodeTreeItem(
        node = NodeUiModel(
            "0x123",
            null,
            children = listOf(),
            isExpanded = false
        ),
        onToggle = {},
        level = 0,
        isCurrent = true
    )
}