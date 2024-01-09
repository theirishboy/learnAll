package com.example.learnwithpierre.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.DeckViewTopAppBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.TopAppBarAddCardScreen
import com.example.learnwithpierre.dao.Deck
import com.example.learnwithpierre.dao.FlashCard
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
    navigateToModifyOneCard: (Long) -> Unit,
    oneDeckViewModel: OneDeckViewModel = viewModel(),
    navigateBack: () -> Unit,
    navigateToTrainView: (Long) -> Unit,
) {
    val oneDeckUiState by oneDeckViewModel.oneDeckUiState.collectAsState()

    when (val state = oneDeckUiState) {
       // is OneDeckUiState.Loading -> LoadingView()
        is OneDeckUiState.Success -> OneDeckViewPage(
            state.deck,
            state.flashCards,
            oneDeckViewModel,
            navigateBack,
            navigateToModifyOneCard,
            navigateToTrainView)
        is OneDeckUiState.Error -> ErrorView(error = state.exception)
    }

}
@Composable
fun ErrorView(error: Throwable) {
    Text(text = "There is an error ${error.message}")
}
@Composable
private fun OneDeckViewPage(
    deck : Deck,
    flashCardsList: MutableList<FlashCard>,
    oneDeckViewModel: OneDeckViewModel,
    navigateBack: () -> Unit,
    navigateToModifyOneCard: (Long) -> Unit,
    navigateToTrainView: (Long) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DeckViewTopAppBar(
                title = "Deck Name",
                canNavigateBack = true,
                navigateBack = navigateBack,
                deleteDeck = { oneDeckViewModel.deleteDeck() },
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.clickable {navigateToTrainView(oneDeckViewModel.getDeckId()) }) {
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

            ExtendedFloatingActionButton(onClick = { showDialog.value = !showDialog.value }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add new deck")
                Text(text = "add card")
            }
        }

    ) {
        DeckViewBody(it, flashCardsList,navigateToModifyOneCard, deleteOneCard = {card -> oneDeckViewModel.deleteOneCard(card)}, deck)
        if(showDialog.value){
            CardDialog(dismissOnBackPress = { showDialog.value = false }, oneDeckViewModel = oneDeckViewModel)
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DeckViewBody(
    it: PaddingValues,
    flashCardsList: MutableList<FlashCard>,
    navigateToModifyOneCard: (Long) -> Unit,
    deleteOneCard: (FlashCard) -> Unit,
    deck: Deck
) {
    val haptics = LocalHapticFeedback.current
    var contextCardId by rememberSaveable { mutableStateOf<Long>(0) }

    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)) {
            Column(Modifier.padding(5.dp)) {
                Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(1.dp))
                Text(text =  deck.description ?: "No description")
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)){
                Text(text = "Cards", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = flashCardsList.size.toString(),modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontSize = 20.sp)

            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(1f),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(flashCardsList) {
                    Row(modifier= Modifier.size(180.dp)){
                        Card( modifier = Modifier
                            .size(180.dp)
                            .padding(10.dp)
                            .combinedClickable(
                                onClick = { navigateToModifyOneCard(it.cardId) },
                                onLongClick = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    contextCardId = it.cardId
                                },
                                onLongClickLabel = "LongClick"
                            ),
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                        ){
                            Column(Modifier.padding(5.dp)){
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(text = it.recto,
                                        modifier = Modifier.padding(start = 2.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 30.sp,
                                        maxLines = 4,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyLarge)

                                }
                            }
                        }

                    }

                    Spacer(modifier = Modifier.padding(3.dp))
                }
            }
        }
        if (contextCardId > 0 && flashCardsList.any{it.cardId == contextCardId}) {
            NoteActionSheet(
                flashCard = flashCardsList.first { it.cardId == contextCardId },
                onDismissSheet = { contextCardId = 0 },
                onDeleteIcon = { deleteOneCard(flashCardsList.first { it.cardId == contextCardId })}
            )
        }

    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteActionSheet(flashCard: FlashCard, onDismissSheet: () -> Unit, onDeleteIcon: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismissSheet){
        ListItem(
            headlineContent = { Text("Delete this card ?") },
            leadingContent = { Icon(painter = painterResource(id = R.drawable.baseline_delete_outline_24), null)},
            modifier = Modifier.clickable(onClick = onDeleteIcon)
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CardDialog(
    dismissOnBackPress: () -> Unit,
    dismissOnClickOutside: Boolean = false,
    oneDeckViewModel: OneDeckViewModel,
    ) {
    var recto by remember { mutableStateOf("") }
    var verso by remember { mutableStateOf("") }
    val addCardToDeck = { oneDeckViewModel.addOneCardToDeck(recto, verso); dismissOnBackPress() }
    Dialog(
            onDismissRequest = { },
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = dismissOnClickOutside,
            )
        ) {
        Card(shape = RoundedCornerShape(15.dp)) {
            Scaffold(
                modifier = Modifier.fillMaxSize(1f),

                topBar = {
                    TopAppBarAddCardScreen(
                        dismissOnBackPress,
                        onSavePress = addCardToDeck,
                        title = "Add Screen"
                    )
                }
            ) {

                    paddingValue ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue)
                ) {
                    Text(text = "Recto", modifier = Modifier.padding(start = 15.dp, top = 10.dp))
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.43f)
                            .padding(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        BasicTextField(
                            value = recto,
                            onValueChange = { recto = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textStyle = TextStyle(color = Color.Black),
                            decorationBox = { innerTextField ->
                                if (recto.isEmpty()) {
                                    // Display a placeholder
                                    Text("Tap here to start typing...")
                                }
                                innerTextField()
                            }
                        )
                    }
                    Text(text = "Verso", modifier = Modifier.padding(start = 15.dp, top = 10.dp))
                    val isError = remember(verso) { verso.length > 10 } // Example condition

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(1f)
                            .padding(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        BasicTextField(
                            value = verso,
                            onValueChange = { verso = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textStyle = TextStyle(color = Color.Black),
                            decorationBox = { innerTextField ->
                                if (verso.isEmpty()) {
                                    // Display a placeholder
                                    Text("Tap here to start typing...")
                                }
                                innerTextField()
                            }
                        )

                    }
                }

            }
        }
    }

}



@Preview
@Composable
fun OneDeckViewScreenPreview() {
    val fcard = FlashCard(0,1,"hello","World",true,"no",5)
    val fcard2 = FlashCard(0,1,"hello","Ce texte est bcp trop long",true,"no",5)
    val deck = Deck(0,0,"Kotlin","")
    DeckViewBody(PaddingValues(5.dp), arrayListOf(fcard,fcard,fcard,fcard2), {},{}, deck)
}
