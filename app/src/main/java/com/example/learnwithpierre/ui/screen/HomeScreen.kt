package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.NavigationBottomBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.Deck
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToProfil: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateToOneDeck: (Long) -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    println("Il y a une recomposition")
    Scaffold(
        bottomBar = {NavigationBottomBar({},navigateToTraining,navigateToProfil,0)

        }
    ) { innerPadding ->
        ShowMyDecksBody(
            homeUiState = homeUiState,
            modifier = modifier.padding(innerPadding),
            navigateToOneDeck = navigateToOneDeck,
            updateFilteredDeck =  {homeViewModel.updateFilteredDeck(it)},
            addNewDeck = {homeViewModel.addNewDeck(it)}
        )
    }

}




@Composable
fun ShowMyDecksBody(
    modifier: Modifier = Modifier,
    navigateToOneDeck: (Long) -> Unit,
    updateFilteredDeck : (String) -> Unit,
    addNewDeck : (Deck) -> Unit,
    homeUiState: HomeUiState
    ){
    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            }
                ,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Card(modifier = modifier
            .fillMaxSize(),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)){

            Column(modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)) {
                Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center,modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp)) {
                    Text(text = "My decks", fontSize = 30.sp, fontWeight = FontWeight.Bold )
                }
            }
            Column(Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f))  {
                Row(verticalAlignment = Alignment.Top,modifier = Modifier
                    .fillMaxSize()){
                    DisplayDecks(homeUiState = homeUiState, navigateToOneDeck = navigateToOneDeck,updateFilteredDeck =  {updateFilteredDeck(it)},
                        addNewDeck = {addNewDeck(it)})
                }}
            }



    }

}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisplayDecks(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    navigateToOneDeck: (Long) -> Unit,
    updateFilteredDeck : (String) -> Unit,
    addNewDeck : (Deck) -> Unit
){
    val localFocusManager = LocalFocusManager.current

    var active by remember {
        mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
                active = false
            })
        }, verticalArrangement = Arrangement.spacedBy(16.dp),) {

        var text by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        TextField(
                value = text,
                onValueChange = {
                    text = it;
                    updateFilteredDeck(it)
                                },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {

                    // Hide the keyboard after submitting the search
                    keyboardController?.hide()
                    //or hide keyboard
                    focusManager.clearFocus()

                })
            )
                LazyColumn(modifier.fillMaxWidth(1f).fillMaxHeight(0.9f)){
                    items(homeUiState.filteredDeckList){
                            item ->
                        OneDeck(item,
                            Modifier
                                .drawBehind {
                                    val strokeWidth = 3 * density
                                    val y = size.height - strokeWidth / 2
                                    drawLine(
                                        Color.LightGray,
                                        Offset(0f, y),
                                        Offset(size.width, y),
                                        strokeWidth
                                    )
                                }
                                .padding(5.dp).fillMaxSize().clickable {navigateToOneDeck(item.deck.deckId)},
                        )

                    }
                }


        DisplayAddButton(addNewDeck,modifier)






    }

}
@Composable
fun DisplayAddButton(addNewDeck : (Deck) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = {  showDialog = true }, shape = RoundedCornerShape(15.dp),modifier = modifier
        .fillMaxSize()
    ) {
        ButtonText(modifier.align(Alignment.CenterVertically)," Create new deck") }
    if (showDialog) {
        AddDeckDialog(
            onDismiss = { showDialog = false },
            onConfirm = { deckName,description ->
                addNewDeck(Deck(0,1,deckName,description, LocalDateTime.now(),
                    LocalDateTime.now()))
                showDialog = false
            }
        )
    }
}

@Composable
fun ButtonText(modifier: Modifier = Modifier, text : String) {
    Row(modifier = modifier){
        Text(text = text)
        Icon(imageVector = Icons.Default.Add, contentDescription = "Create new deck")
    }

}

@Composable
fun OneDeck(deckWithSize: DeckWithSize,modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(0.7f)) {
            Text(text = deckWithSize.deck.name, fontWeight = FontWeight.Bold ,maxLines = 1, modifier = Modifier.fillMaxWidth())
            Row{
                Icon(
                    painter = painterResource(id = R.drawable.playing_cards_24),
                    contentDescription = "Last day you used the app"
                )
                Text(text = deckWithSize.size.toString(), modifier = Modifier.fillMaxWidth())
            }
        }
        Column(modifier = Modifier.weight(0.3f)) {
            Row(modifier = Modifier.align(Alignment.End)) {

                Text(text = ChronoUnit.DAYS.between(deckWithSize.deck.updatedAt, LocalDateTime.now()).toString(), textAlign = TextAlign.End)
                Icon(painter = painterResource(id = R.drawable.baseline_history_24)
                    , contentDescription = "Last day you used the app")

            }

        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ShowDeckScrenPreview() {
    LearnWithPierreTheme() {
 //       ShowMyDecksBody(
          //  homeViewModel =  HomeViewModel(),

         //   modifier = Modifier,)


    }
}
@Composable
fun AddDeckDialog(onDismiss: () -> Unit, onConfirm: (String,String) -> Unit) {
    var deckName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium, // Rounded corners
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Create New Deck",
                )
                OutlinedTextField(
                    value = deckName,
                    onValueChange = { deckName = it },
                    label = { Text("Deck Name") },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    maxLines = 10,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(deckName,description) },
                        enabled = deckName.isNotBlank() // Disable if the input is blank
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
@Preview
@Composable
private fun OneDeckPreview(){
    LearnWithPierreTheme {
        val deck =  Deck(1,2,"jean","la description est longue", LocalDateTime.now(), LocalDateTime.now())
        val deckWithSize = DeckWithSize(deck,2)
        OneDeck(deckWithSize = deckWithSize)
    }

}
@Preview
@Composable
private fun MyDecksBodyPreview(){
    LearnWithPierreTheme {
        ShowMyDecksBody(Modifier,{},{},{},HomeUiState())
    }

}

