package ru.kropotov.denet.test.compose.node

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kropotov.denet.test.R
import ru.kropotov.denet.test.data.node.Node
import ru.kropotov.denet.test.ui.theme.LocalCustomColors
import ru.kropotov.denet.test.ui.theme.buttonLarge
import ru.kropotov.denet.test.ui.theme.buttonShape
import ru.kropotov.denet.test.ui.theme.titleMedium
import ru.kropotov.denet.test.ui.theme.titleSmall
import ru.kropotov.denet.test.viewmodel.NodeViewModel

@Composable
fun NodeScreen(
    modifier: Modifier = Modifier,
    viewModel: NodeViewModel = hiltViewModel(),
    toNavigate: (String) -> Unit = {}
) {
    val node by viewModel.node.collectAsStateWithLifecycle()
    node?.let {
        NodeContent(
            modifier = modifier,
            node = it,
            onNavigationClicked = toNavigate
        )
    }
}

@Composable
fun NodeContent(
    modifier: Modifier = Modifier,
    node: Node,
    onNavigationClicked: (String) -> Unit
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NodeContent(
                node = node,
                isLandscape = isLandscape,
                modifier = Modifier.fillMaxWidth()
            )

            val childrenCount = node.children.count()
            NavigationButtons(
                parent = node.parent,
                children = node.children,
                onNavigationClicked = onNavigationClicked,
                onNextClicked = {
                    if (childrenCount > 1) {
                        isBottomSheetVisible = true
                    } else if (childrenCount == 1) {
                        onNavigationClicked(node.children[0])
                    }
                }
            )
        }

        if (isBottomSheetVisible) {
            NodeNextBottomSheet(
                node = node,
                onItemSelected = { onNavigationClicked(it) },
                onDismiss = { isBottomSheetVisible = false }
            )
        }
    }
}

@Composable
fun ColumnScope.NodeContent(
    node: Node,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    val title = if (node.parent == null) {
        stringResource(R.string.node_type_root)
    } else {
        stringResource(R.string.node_type_regular)
    }

    val shape = @Composable {
        AbstractNodeShape(
            seed = node.address,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    val textBlock = @Composable {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = if (isLandscape) Alignment.Start else Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = node.address,
                style = titleMedium.copy(
                    color = LocalCustomColors.current.accent,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

    if (isLandscape) {
        Row(
            modifier = modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            shape()
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                textBlock()
            }
        }
    } else {
        Column(
            modifier = modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.8f))
            shape()
            Spacer(modifier = Modifier.width(16.dp))
            textBlock()
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun NavigationButtons(
    parent: String?,
    children: List<String>?,
    onNavigationClicked: (String) -> Unit,
    onNextClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (parent != null) {
            NodeButton(
                text = stringResource(R.string.button_back),
                onClick = { onNavigationClicked(parent) },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (children != null && children.isNotEmpty()) {
            val text = if (children.count() == 1) {
                stringResource(R.string.button_next)
            } else {
                stringResource(R.string.button_next_multiple).format(children.count())
            }
            NodeButton(
                text = text,
                onClick = { onNextClicked() },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun RowScope.NodeButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
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

@Preview
@Composable
fun NodeScreenPreviewPortrait() {
    val node = Node(
        address = "0x21c5983b5ee94b6cf7b3a041cae7d830566ac443",
        children = listOf("0x456"),
        parent = "0xabc"
    )
    NodeContent(node = node, onNavigationClicked = {})
}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun NodeScreenPreviewLandscape() {
    val node = Node(
        address = "0x21c5983b5ee94b6cf7b3a041cae7d830566ac443",
        children = listOf("0x456"),
        parent = "0xabc"
    )
    NodeContent(node = node, onNavigationClicked = {})
}