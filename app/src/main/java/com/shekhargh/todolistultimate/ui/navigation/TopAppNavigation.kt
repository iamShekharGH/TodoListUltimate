package com.shekhargh.todolistultimate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shekhargh.todolistultimate.ui.composables.AddEditScreen
import com.shekhargh.todolistultimate.ui.composables.MainScreen

@Composable
fun TopAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeScreen.route
    ) {
        composable(HomeScreen.route) {
            MainScreen(
                onNavigateToTask = { taskId ->
                    navController.navigate(AddItemScreen.withId(taskId))

                }
            )
        }
        composable(
            route = AddItemScreen.routeWithArg,
            arguments = AddItemScreen.arguments
        ) { AddEditScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        ) }

    }

}

interface Destinations {
    val route: String
}

object HomeScreen : Destinations {
    override val route: String
        get() = "home_screen"

}

object AddItemScreen : Destinations {
    override val route: String = "add_item_screen"
    const val taskIdArg = "task_id"
    val routeWithArg = "${route}?$taskIdArg={$taskIdArg}"
    val arguments = listOf(
        navArgument(taskIdArg) {
            type = NavType.IntType
            defaultValue = -1
        }
    )

    fun withId(id: Int): String {
        return "$route?$taskIdArg=$id"
    }
}