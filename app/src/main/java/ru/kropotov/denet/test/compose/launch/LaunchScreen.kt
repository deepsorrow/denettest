package ru.kropotov.denet.test.compose.launch

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kropotov.denet.test.viewmodel.LaunchViewModel

@Composable
fun LaunchScreen(
    viewModel: LaunchViewModel = hiltViewModel(),
    navigateTo: (String) -> Unit = {},
) {
    val startNode = viewModel.startNode.collectAsStateWithLifecycle().value
    startNode?.let { navigateTo(it) }
}