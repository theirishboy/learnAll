package com.example.learnwithpierre.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learnwithpierre.model.DataProvider
import com.example.learnwithpierre.ui.screen.AuthViewModel
import com.example.learnwithpierre.ui.screen.HomeDestination
import com.example.learnwithpierre.ui.screen.HomeScreen
import com.example.learnwithpierre.ui.screen.HomeViewModel
import com.example.learnwithpierre.ui.screen.OneCardViewDestination
import com.example.learnwithpierre.ui.screen.OneCardViewModel
import com.example.learnwithpierre.ui.screen.OneCardViewScreen
import com.example.learnwithpierre.ui.screen.OneDeckViewDestination
import com.example.learnwithpierre.ui.screen.OneDeckViewModel
import com.example.learnwithpierre.ui.screen.OneDeckViewScreen
import com.example.learnwithpierre.ui.screen.ProfileViewDestination
import com.example.learnwithpierre.ui.screen.ProfileViewScreen
import com.example.learnwithpierre.ui.screen.TrainDestination
import com.example.learnwithpierre.ui.screen.TrainScreen
import com.example.learnwithpierre.ui.screen.TrainViewModel


@Composable
fun LearnAllNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val currentUser = authViewModel.currentUser.collectAsState().value
    DataProvider.updateAuthState(currentUser)

    NavHost(
        navController = navController,
        startDestination = ProfileViewDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            val homeScreenViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                modifier = modifier,
                navigateToProfil = { navController.navigate(ProfileViewDestination.route) },
                navigateToTraining = { navController.navigate("${TrainDestination.route}/0")},
                navigateToOneDeck = {deckId -> navController.navigate("${OneDeckViewDestination.route}/$deckId")},
                homeViewModel = homeScreenViewModel
            )
        }
        composable(route = TrainDestination.routeWithArgs) {
            val trainViewModel = hiltViewModel<TrainViewModel>()
            TrainScreen(
                navigateBack = {navController.navigate(HomeDestination.route)
                },
                trainViewModel = trainViewModel

            )
        }

        composable(route = OneDeckViewDestination.routeWithArgs){
            val oneDeckViewModel = hiltViewModel<OneDeckViewModel>()
            OneDeckViewScreen(navigateBack = {navController.popBackStack(HomeDestination.route, inclusive = false)
                },
                navigateToAllCards = { /*TODO*/ },
                navigateToTraining =  {},
                navigateToModifyOneCard = {cardId -> navController.navigate("${OneCardViewDestination.route}/$cardId")},
                navigateToTrainView = {deckId -> navController.navigate("${TrainDestination.route}/$deckId")},
                oneDeckViewModel = oneDeckViewModel

            )
        }
        composable(route = OneCardViewDestination.routeWithArgs){
            val oneCardViewModel = hiltViewModel<OneCardViewModel>()
            OneCardViewScreen(dismissOnBackPress = {navController.popBackStack("${OneDeckViewDestination.route}/1",
                inclusive = false)
                },
                oneCardViewModel = oneCardViewModel
            )
        }
        composable(route = ProfileViewDestination.route){
        // Creates a ViewModel from the current BackStackEntry
            ProfileViewScreen(authViewModel,
                navigateToHomeScreen = {navController.navigate(HomeDestination.route)},
                navigateToTraining = { navController.navigate("${TrainDestination.route}/0")},)
        }
    }

}

