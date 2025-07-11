package ru.kropotov.denet.test.compose.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kropotov.denet.test.compose.common.BottomSheetSelect
import ru.kropotov.denet.test.compose.node.AbstractNodeShape
import ru.kropotov.denet.test.ui.theme.buttonMedium
import ru.kropotov.denet.test.ui.theme.buttonShape
import ru.kropotov.denet.test.viewmodel.EditorViewModel

@Composable
fun EditorBottomSheet(
    viewModel: EditorViewModel = hiltViewModel(),
    node: NodeUiModel,
    onDismiss: () -> Unit
) {
    var openCreateNodeDialog by remember { mutableStateOf(false) }
    val parents = viewModel.pathToCurrentNode.collectAsStateWithLifecycle().value

    val availableActions = if (node.address in parents) {
        NodeEditAction.entries.filter { it != NodeEditAction.DELETE }
    } else {
        NodeEditAction.entries
    }
    BottomSheetSelect(
        items = availableActions,
        onDismiss = { onDismiss() },
        title = {
            EditorBottomSheetTitle(node)
        },
        itemContent = { editAction ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF2A2F3A), shape = buttonShape)
                    .clickable {
                        if (editAction == NodeEditAction.DELETE) {
                            viewModel.deleteNode(address = node.address)
                            onDismiss()
                        } else if (editAction == NodeEditAction.ADD_CHILD) {
                            openCreateNodeDialog = true
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(editAction.actionRes),
                    color = Color.White,
                    style = buttonMedium,
                    overflow = TextOverflow.MiddleEllipsis,
                    maxLines = 1
                )
            }
        }
    )

    if (openCreateNodeDialog) {
        EditorCreateNodeDialog(
            onDismissRequest = { openCreateNodeDialog = false },
            onConfirmation = {
                viewModel.createChildNode(parent = node.address, childAddress = it)
                openCreateNodeDialog = false
            }
        )
    }
}

@Composable
fun EditorBottomSheetTitle(node: NodeUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AbstractNodeShape(seed = node.address, size = 48.dp, isMinimized = true)

        Spacer(Modifier.width(8.dp))

        Text(
            text = node.address,
            color = Color.White,
            style = buttonMedium,
            overflow = TextOverflow.MiddleEllipsis,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun EditorBottomSheetPreview() {
    EditorBottomSheet(
        node = NodeUiModel(
            address = "123",
            parent = null,
            children = listOf()
        )
    ) { }
}