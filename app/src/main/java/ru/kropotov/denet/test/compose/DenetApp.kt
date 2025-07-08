package ru.kropotov.denet.test.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.kropotov.denet.test.compose.editor.EditorScreen
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
            NodeScreen(
                navigateTo = { navController.navigateToNode(it) }
            )
        }

        composable(route = Screen.Editor.route) {
            EditorScreen(
                onBackClick = {
                    navController.navigate(
                        route = Screen.Node.createRoute(it.address)
                    )
                }
            )
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