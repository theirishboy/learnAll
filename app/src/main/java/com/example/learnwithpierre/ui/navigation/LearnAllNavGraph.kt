package com.example.learnwithpierre.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learnwithpierre.ui.screen.HomeDestination
import com.example.learnwithpierre.ui.screen.HomeScreen
import com.example.learnwithpierre.ui.screen.OneDeckViewDestination
import com.example.learnwithpierre.ui.screen.OneDeckViewScreen
import com.example.learnwithpierre.ui.screen.ShowAllCardScreen
import com.example.learnwithpierre.ui.screen.ShowAllDataScreenDestination
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
                modifier = modifier,
                navigateToAllCards = { navController.navigate(ShowAllDataScreenDestination.route) },
                navigateToTraining = { navController.navigate(TrainDestination.route)},
                navigateToOneDeck = {deckId -> navController.navigate("${OneDeckViewDestination.route}/$deckId")}
            )
        }
        composable(route = TrainDestination.route) {
            TrainScreen(
                navigateBack = {     navController.popBackStack(HomeDestination.route, inclusive = false)
                },
                navigateToHome = { navController.navigate(HomeDestination.route) }
            )
        }
        composable(route = ShowAllDataScreenDestination.route) {
            ShowAllCardScreen(
                navigateToTraining = { navController.navigate(TrainDestination.route)},
                navigateToHomeScreen = {navController.navigate(HomeDestination.route)}
            )
        }
        composable(route = OneDeckViewDestination.routeWithArgs){
            OneDeckViewScreen(navigateToAllCards = { /*TODO*/ },
                navigateToTraining =  {}
            )
        }
    }

}

