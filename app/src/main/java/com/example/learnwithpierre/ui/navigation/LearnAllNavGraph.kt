package com.example.learnwithpierre.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learnwithpierre.ui.screen.AuthViewModel
import com.example.learnwithpierre.ui.screen.HomeDestination
import com.example.learnwithpierre.ui.screen.HomeScreen
import com.example.learnwithpierre.ui.screen.OneCardViewDestination
import com.example.learnwithpierre.ui.screen.OneCardViewScreen
import com.example.learnwithpierre.ui.screen.OneDeckViewDestination
import com.example.learnwithpierre.ui.screen.OneDeckViewScreen
import com.example.learnwithpierre.ui.screen.ProfileViewDestination
import com.example.learnwithpierre.ui.screen.ProfileViewScreen
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
        startDestination = ProfileViewDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                modifier = modifier,
                navigateToProfil = { navController.navigate(ProfileViewDestination.route) },
                navigateToTraining = { navController.navigate("${TrainDestination.route}/0")},
                navigateToOneDeck = {deckId -> navController.navigate("${OneDeckViewDestination.route}/$deckId")}
            )
        }
        composable(route = TrainDestination.routeWithArgs) {
            TrainScreen(
                navigateBack = {     navController.popBackStack(HomeDestination.route, inclusive = false)
                }
            )
        }
        composable(route = ShowAllDataScreenDestination.route) {
            ShowAllCardScreen(
                navigateToTraining = { navController.navigate(TrainDestination.route)},
                navigateToHomeScreen = {navController.navigate(HomeDestination.route)}
            )
        }
        composable(route = OneDeckViewDestination.routeWithArgs){
            OneDeckViewScreen(navigateBack = {     navController.popBackStack(HomeDestination.route, inclusive = false)
                },
                navigateToAllCards = { /*TODO*/ },
                navigateToTraining =  {},
                navigateToModifyOneCard = {cardId -> navController.navigate("${OneCardViewDestination.route}/$cardId")},
                navigateToTrainView = {deckId -> navController.navigate("${TrainDestination.route}/$deckId")}

            )
        }
        composable(route = OneCardViewDestination.routeWithArgs){
            OneCardViewScreen(dismissOnBackPress = {navController.popBackStack("${OneDeckViewDestination.route}/1", inclusive = false)
                },
            )
        }
        composable(route = ProfileViewDestination.route){
        // Creates a ViewModel from the current BackStackEntry
            val viewModel = hiltViewModel<AuthViewModel>()
            ProfileViewScreen(viewModel,
                navigateToHomeScreen = {navController.navigate(HomeDestination.route)},
                navigateToTraining = { navController.navigate("${TrainDestination.route}/0")},)
        }
    }

}

