package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.R
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.manageData.CardUiState
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navigateToAllCards: () -> Unit,
    navigateToTraining: () -> Unit,
    viewModel: CardEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Save","Train","Data")
    val icons = listOf(R.drawable.baseline_download_24,R.drawable.baseline_model_training_24,R.drawable.baseline_dataset_24)
    val saveState = viewModel.saveUiState
    val cardEntryUiState by viewModel.cardEntryUiState.collectAsState()
    val navigationScreens = listOf({},navigateToTraining,navigateToAllCards)
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {Icon(painter = painterResource(id = icons[index]), contentDescription ="" )},
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = navigationScreens[index]
                    )
                }
            }
        }
    ) { innerPadding ->
        ShowMyDecksBody(
            dataUiState = viewModel.cardUiState,
            onValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveData()
                }
            },
            saveState = saveState,
            modifier = modifier.padding(innerPadding),
            categories = cardEntryUiState.filterCategories
        )
    }

}




@Composable
fun ShowMyDecksBody(
    dataUiState: CardUiState,
    onValueChange: (CardUiState) -> Unit,
    onSaveClick: () -> Unit,
    saveState: SaveState,
    modifier: Modifier = Modifier,
    categories: List<String>,


    ){
    val localFocusManager = LocalFocusManager.current

    Column(
        modifier = modifier
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
        Card(modifier = Modifier
            .fillMaxWidth()
            .weight(0.5f),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)){


            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,modifier = modifier
                    .fillMaxWidth()
                    .padding(3.dp)) {
                Text(text = "My decks", fontSize = 30.sp, fontWeight = FontWeight.Bold )
            }
            Row(modifier = modifier.weight(1f)){
                //Spacer(modifier = Modifier.height(100.dp))
                SaveForm2(dataUiState = dataUiState, onValueChange = onValueChange, saveState = saveState, categories = categories)

            }}

    }

}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SaveForm2(dataUiState: CardUiState,
             modifier: Modifier = Modifier,
             onValueChange: (CardUiState) -> Unit = {},
             enabled: Boolean = true,
             saveState: SaveState,
             categories: List<String>
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current


    var active by remember {
        mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxWidth()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
                active = false
            })
        }
      , verticalArrangement = Arrangement.spacedBy(16.dp),) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = dataUiState.category,
            onQueryChange = {  onValueChange(dataUiState.copy(category = it)) } ,
            onSearch = {active = false}, active = active ,
            onActiveChange = { active = it },
            placeholder = { Text(text = "Decks")},
            leadingIcon = {Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")},
            trailingIcon = {
                if(active) {
                        Icon(
                            modifier = Modifier.clickable {
                                onValueChange(dataUiState.copy(category = ""))

                            },
                            imageVector = Icons.Default.Close, contentDescription = "add Icon"
                        )
                }

            },

        ) {
            LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {

            items(items = categories, key = {it}) {
                Row(modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth()
                    .clickable {
                        onValueChange(dataUiState.copy(category = it)); active = false
                    }) {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.Search, contentDescription = "history Icon"
                    )
                    Text(text = it)

                }
            }
            }

        }
        val itemsList = listOf("A", "B", "C")

        LazyColumn(modifier.weight(0.7f)){
            items(itemsList){
                OneDeck(
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
                        .padding(5.dp))

            }
        }
        Row(
            Modifier
                .weight(0.1f)
                .fillMaxHeight().padding(5.dp)) {
            Button(onClick = { /*TODO*/ }, shape = RoundedCornerShape(15.dp),modifier = Modifier.fillMaxWidth().align(Alignment.Bottom)) {
                CreateNewDeck(modifier.align(Alignment.Bottom))

            }

        }


    }

}

@Composable
fun CreateNewDeck(modifier: Modifier = Modifier) {
    Row(modifier = modifier){
        Text(text = " Create new deck")
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add new deck")
    }

}

@Composable
fun OneDeck(modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(0.7f)) {
            Text(text = "Name Dweqewqee weqewqwqweqew eck", fontWeight = FontWeight.Bold ,maxLines = 1, modifier = Modifier.fillMaxWidth())
            Row{
                Icon(
                    painter = painterResource(id = R.drawable.playing_cards_24),
                    contentDescription = "Last day you used the app"
                )
                Text(text = "50 cards", modifier = Modifier.fillMaxWidth())
            }
        }
        Column(modifier = Modifier.weight(0.3f)) {
            Row(modifier = Modifier.align(Alignment.End)) {

                Text(text = "50 day ", textAlign = TextAlign.End)
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
        ShowMyDecksBody(
            dataUiState = CardUiState(
                recto = "Item name et si on met un item name vraiement long pour avoir un test en condition reelzeaeazeazeaze",
                verso = "10.00 et bas ça a l'air pas mal tout ca, vraiment même les longs textes sont corrects",
            ),
            onValueChange = {},
            onSaveClick = {},
            saveState = SaveState.SHOWSUCCESS,
            modifier = Modifier,
            categories = listOf("Litterature moderne","roman"))
    }
}
@Preview
@Composable
private fun OneDeckPreview(){
    LearnWithPierreTheme {
        OneDeck()
    }

}

