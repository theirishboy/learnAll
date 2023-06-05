package com.example.learnwithpierre.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learnwithpierre.ui.screen.HomeDestination
import com.example.learnwithpierre.ui.screen.HomeScreen
import com.example.learnwithpierre.ui.screen.TrainDestination
import com.example.learnwithpierre.ui.screen.TrainScreen


@Composable
fun LearnAllNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateBack = { navController.popBackStack() },
                navigateToTraining = { navController.navigate(TrainDestination.route) }
            )
        }
        composable(route = TrainDestination.route) {
            TrainScreen(
                navigateBack = { navController.popBackStack() },
              //  navigateToTraining = { navController.navigateUp() }
            )
        }
    }

}
     /*   composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
           //     onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}*///