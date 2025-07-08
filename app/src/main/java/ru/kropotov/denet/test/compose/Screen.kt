package ru.kropotov.denet.test.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Launch : Screen("launch")

    data object Node : Screen(
        route = "node/{nodeAddress}",
        navArguments = listOf(navArgument("nodeAddress") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(nodeAddress: String) = "node/${nodeAddress}"
    }

    data object Editor : Screen("editor")
}