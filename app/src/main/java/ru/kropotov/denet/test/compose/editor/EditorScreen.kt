import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kropotov.denet.test.compose.editor.NodeTreeItem
import ru.kropotov.denet.test.compose.editor.NodeUiModel
import ru.kropotov.denet.test.compose.editor.NodeUiModel.Companion.toNodeUiModel
import ru.kropotov.denet.test.compose.editor.NodeUiModel.Companion.toSavable
import ru.kropotov.denet.test.data.node.Node
import ru.kropotov.denet.test.viewmodel.EditorViewModel

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val nodes = viewModel.nodes.collectAsStateWithLifecycle().value ?: return
    val currentNode = viewModel.currentNode.collectAsStateWithLifecycle().value ?: return
    val parentsCurrentNode = viewModel.pathToCurrentNode.collectAsStateWithLifecycle().value
    EditorScreen(
        modifier = modifier,
        nodes = nodes,
        parentsCurrentNode = parentsCurrentNode,
        currentNode = currentNode
    )
}

@Composable
fun EditorScreen(
    modifier: Modifier,
    nodes: List<Node>,
    parentsCurrentNode: List<String>,
    currentNode: Node
) {
    val uiNodes = rememberSaveable(
        saver = listSaver(
            save = { list -> list.map { it.toSavable() } },
            restore = { savedList -> savedList.map { it.toNodeUiModel() }.toMutableStateList() }
        )
    ) { mutableStateListOf<NodeUiModel>() }

    var expandOnFirstLaunch by rememberSaveable { mutableStateOf(true) }

    // resolve changes
    LaunchedEffect(nodes) {
        val newMap = nodes.associateBy { it.address }
        val oldMap = uiNodes.associateBy { it.address }

        for (newNode in nodes) {
            val oldNode = oldMap[newNode.address]

            if (oldNode == null) {
                val newUiNode = NodeUiModel(
                    address = newNode.address,
                    parent = newNode.parent,
                    children = newNode.children,
                    isExpanded = false
                )
                uiNodes.add(newUiNode)
            } else if (newNode.children != oldNode.children) {
                val updated = oldNode.copy(children = newNode.children)
                val index = uiNodes.indexOfFirst { it.address == oldNode.address }
                if (index >= 0) uiNodes[index] = updated
            }
        }

        val addressesInNew = newMap.keys
        uiNodes.removeAll { it.address !in addressesInNew }
    }

    val rootNode = uiNodes.firstOrNull { it.parent == null } ?: return

    // expand all nodes to current on first launch
    if (expandOnFirstLaunch) {
        LaunchedEffect(Unit) {
            parentsCurrentNode.forEach { addressToExpand ->
                val index = uiNodes.indexOfFirst { it.address == addressToExpand }
                uiNodes[index] = uiNodes[index].copy(isExpanded = true)
            }
        }
        @Suppress("AssignedValueIsNeverRead")
        expandOnFirstLaunch = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF11151C))
            .padding(16.dp)
    ) {
        ExpandableNodeTree(
            rootNode = rootNode,
            currentNodeAddress = currentNode.address,
            getNode = { address -> uiNodes.firstOrNull { it.address == address } },
            onToggle = { node ->
                val index = uiNodes.indexOf(node)
                uiNodes[index] = uiNodes[index].copy(isExpanded = !node.isExpanded)
            }
        )
    }
}

@Composable
fun ExpandableNodeTree(
    rootNode: NodeUiModel,
    currentNodeAddress: String,
    level: Int = 0,
    getNode: (String) -> NodeUiModel?,
    onToggle: (NodeUiModel) -> Unit
) {
    Column {
        NodeTreeItem(
            node = rootNode,
            onToggle = onToggle,
            level = level,
            isCurrent = rootNode.address == currentNodeAddress
        )
        if (rootNode.isExpanded) {
            rootNode.children.forEach { childAddress ->
                getNode(childAddress)?.let { node ->
                    ExpandableNodeTree(
                        rootNode = node,
                        getNode = getNode,
                        currentNodeAddress = currentNodeAddress,
                        level = level + 1,
                        onToggle = onToggle
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EditorScreenPreview() {
    val rootNode = Node(address = "0xab...dacb", children = listOf("0xbs...3asc"), parent = null)
    val childNode = Node(address = "0xbs...3asc", children = listOf(), parent = "0xab...dacb")
    EditorScreen(
        modifier = Modifier,
        parentsCurrentNode = listOf("0xab...dacb"),
        nodes = listOf(rootNode, childNode),
        currentNode = childNode
    )
}