package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.navigation.NavigationDestination

object OneDeckViewDestination : NavigationDestination {
    override val route = "DeckView"
    override val titleRes: Int = R.string.app_name
    const val deckIdArg = "deckId"
    val routeWithArgs = "$route/{$deckIdArg}"
}

@Composable
fun OneDeckViewScreen(
    navigateToAllCards: () -> Unit,
    navigateToTraining: () -> Unit,
    oneDeckViewModel: OneDeckViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val oneDeckUiState by oneDeckViewModel.oneDeckUiState.collectAsState()

    when (val state = oneDeckUiState) {
       // is OneDeckUiState.Loading -> LoadingView()
        is OneDeckUiState.Success -> OneDeckViewPage(state.flashCards)
        is OneDeckUiState.Error -> ErrorView(error = state.exception)
    }

}
@Composable
fun ErrorView(error: Throwable) {
    Text(text = "There is an error ${error.message}")
}
@Composable
private fun OneDeckViewPage(oneDeckUiState: MutableList<FlashCard>) {
    Scaffold(
        topBar = {
            LearnAllTopAppBar(
                title = "Deck Name",
                canNavigateBack = true,
                navigateUp = {},
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.clickable { }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_book_24),
                        contentDescription = "Add new deck"
                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    Text(
                        text = "Let's Train",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, textAlign = TextAlign.Center
                    )
                }

            }

        },
        floatingActionButton = {

            ExtendedFloatingActionButton(onClick = { /* do something */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add new deck")
                Text(text = "add card")
            }
        }

    ) {
        DeckViewBody(it, oneDeckUiState)


    }
}

@Composable
private fun DeckViewBody(it: PaddingValues, oneDeckUiState: MutableList<FlashCard>) {
    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(5.dp)) {
                Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(1.dp))
                Text(text = "Je décris la descirption")
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth()){
                Text(text = "Cards", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "3/5",modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontSize = 20.sp)

            }
            val listOfDeck = listOf(
                FlashCard(1, 1, "Est ce aue Kotlin est pour les gens", "dee", true, "none", 5),
                FlashCard(1, 1, "Est ce aue Kotlin est pour les gens", "dee", true, "none", 5),
                FlashCard(1, 1, "Est ce aue Kotlin est pour les gens", "dee", true, "none", 5),
                FlashCard(1, 1, "Est ce aue Kotlin est pour les gens", "dee", true, "none", 5),
                FlashCard(1, 1, "Est ce aue Kotlin est pour les gens", "dee", true, "none", 5)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(1f),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(oneDeckUiState) {
                    Row{
                        Card( modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .weight(0.5f),
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                        ){
                            Column(Modifier.padding(5.dp)){

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(text = it.recto,
                                        modifier = Modifier.padding(start = 2.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 30.sp,
                                        style = MaterialTheme.typography.bodyLarge)

                                }
                            }
                        }

                    }

                    Spacer(modifier = Modifier.padding(3.dp))
                }
            }
        }
    }


}

@Preview
@Composable
fun OneDeckViewScreenPreview() {
    OneDeckViewScreen({}, {})

}