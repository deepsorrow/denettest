package ru.kropotov.denet.test.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.kropotov.denet.test.data.node.Node
import ru.kropotov.denet.test.data.node.NodeRepository
import javax.inject.Inject
import androidx.lifecycle.viewModelScope

@HiltViewModel
class NodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    nodeRepository: NodeRepository
) : ViewModel() {

    val nodeAddress: String = savedStateHandle.get<String>(NODE_ADDRESS)!!

    val node: StateFlow<Node?> = nodeRepository.getNode(nodeAddress)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    companion object {
        private const val NODE_ADDRESS = "nodeAddress"
    }
}