package ru.kropotov.denet.test.compose

import EditorScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.kropotov.denet.test.compose.launch.LaunchScreen
import ru.kropotov.denet.test.compose.node.NodeScreen

@Composable
fun DenetApp() {
    val navController = rememberNavController()
    DenetNavHost(navController)
}

@Composable
fun DenetNavHost(
    navController: NavHostController
) {
    var toolbarState by remember { mutableStateOf<ToolbarState?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                state = toolbarState
            )
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Launch.route) {
            composable(route = Screen.Launch.route) {
                LaunchScreen(
                    navigateTo = {
                        navController.navigateToNode(it) { popUpTo(0) }
                    }
                )
            }

            composable(
                route = Screen.Node.route,
                arguments = Screen.Node.navArguments
            ) {
                toolbarState = ToolbarState.NodeScreenState
                NodeScreen(
                    modifier = Modifier.padding(paddingValues = innerPadding),
                    toNavigate = {
                        navController.navigateToNode(it)
                    }
                )
            }

            composable(route = Screen.Editor.route) {
                toolbarState = ToolbarState.EditorScreenState
                EditorScreen(
                    modifier = Modifier.padding(paddingValues = innerPadding)
                )
            }
        }
    }
}

fun NavController.navigateToNode(address: String, builder: NavOptionsBuilder.() -> Unit = { }) {
    val targetEntry = backQueue
        .findLast {
            it.destination.route == Screen.Node.route &&
            it.arguments?.getString("nodeAddress") == address
        }

    if (targetEntry != null) {
        while (backQueue.last() != targetEntry) {
            if (!popBackStack()) return
        }
        return
    } else {
        navigate(route = Screen.Node.createRoute(address)) {
            builder()
        }
    }
    return
}