package ru.kropotov.denet.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kropotov.denet.test.data.node.Node
import ru.kropotov.denet.test.data.node.NodeRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EditorViewModel @Inject constructor(
    private val nodeRepository: NodeRepository
) : ViewModel() {

    val nodes: StateFlow<List<Node>?> =
        nodeRepository.getNodes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val currentNode: StateFlow<Node?> =
        nodeRepository.getLastNodeAddress().flatMapLatest { address ->
            if (address != null) {
                nodeRepository.getNode(address)
            } else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    /**
     * Parents of the current node.
     */
    val pathToCurrentNode: StateFlow<List<String>> =
        combine(currentNode, nodes) { currentNode, nodes ->
            generateSequence(currentNode?.address) { address ->
                nodes?.find { it.address == address }?.parent
            }.toList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    fun deleteNode(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val deletedNodes = nodeRepository.deleteNode(address)

            // move to undeleted node
            if (currentNode.value?.address in deletedNodes) {
                val rootNodeAddress = nodeRepository.getRootNode().first()?.address
                nodeRepository.saveLastNode(rootNodeAddress)
            }
        }
    }

    fun createChildNode(parent: String, childAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            nodeRepository.addChildNode(parent, childAddress)
        }
    }
}