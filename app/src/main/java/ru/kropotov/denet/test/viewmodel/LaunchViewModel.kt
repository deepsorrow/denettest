package ru.kropotov.denet.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import ru.kropotov.denet.test.data.launch.LaunchRepository
import ru.kropotov.denet.test.data.node.NodeRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val launchRepository: LaunchRepository,
    private val nodeRepository: NodeRepository
) : ViewModel() {

    private val _startNode: MutableStateFlow<String?> = MutableStateFlow(null)
    val startNode: StateFlow<String?> = _startNode

    init {
        viewModelScope.launch {
            launchRepository.getSavedState().flatMapLatest { savedState ->
                if (savedState?.lastNodeAddress == null) {
                    nodeRepository.getRootNode().map { it?.address }
                } else {
                    flowOf(savedState.lastNodeAddress)
                }
            }.collect { address ->
                _startNode.emit(address)
            }
        }
    }

}