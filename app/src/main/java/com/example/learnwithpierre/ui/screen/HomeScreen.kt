package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.R
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.manageData.DataEntryViewModel
import com.example.learnwithpierre.ui.manageData.DataUiState
import com.example.learnwithpierre.ui.manageData.SaveState
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
    navigateToAllData: () -> Unit,
    navigateToTraining: () -> Unit,
    viewModel: DataEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Save","Train","Data")
    val icons = listOf(R.drawable.baseline_download_24,R.drawable.baseline_model_training_24,R.drawable.baseline_dataset_24)
    val saveState = viewModel.saveUiState
    val dataEntryUiState by viewModel.dataEntryUiState.collectAsState()
    val navigationScreens = listOf({},navigateToTraining,navigateToAllData)
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
        SaveBody(
            dataUiState = viewModel.dataUiState,
            onValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveData()
                }
            },
            saveState = saveState,
            modifier = modifier.padding(innerPadding),
            categories = dataEntryUiState.filterCategories
        )
    }

}




@Composable
fun SaveBody(
    dataUiState: DataUiState,
    onValueChange: (DataUiState) -> Unit,
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
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)){


            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,modifier = modifier
                    .fillMaxWidth()
                    .weight(0.3f)) {
                Text(text = "ajouter une carte")
            }
            Row(modifier = modifier.weight(1f)){
                //Spacer(modifier = Modifier.height(100.dp))
                SaveForm(dataUiState = dataUiState, onValueChange = onValueChange, saveState = saveState, categories = categories)

            }}

        Button(
            onClick = onSaveClick,
            enabled = dataUiState.actionEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }




    }

}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SaveForm(dataUiState: DataUiState,
             modifier: Modifier = Modifier,
             onValueChange: (DataUiState) -> Unit = {},
             enabled: Boolean = true,
             saveState: SaveState,
             categories: List<String>
){
    val keyboardController = LocalSoftwareKeyboardController.current

    var active by remember {
        mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()
      , verticalArrangement = Arrangement.spacedBy(16.dp),) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = dataUiState.category,
            onQueryChange = {  onValueChange(dataUiState.copy(category = it)) } ,
            onSearch = {active = false}, active = active ,
            onActiveChange = { active = it },
            placeholder = { Text(text = "Catégorie")},
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

            }
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
        OutlinedTextField(
            value = dataUiState.recto,
            onValueChange = { onValueChange(dataUiState.copy(recto = it)) },
            label = { Text("Recto") },
            modifier = Modifier
                .fillMaxWidth(),
              //  .weight(1f),
            enabled = enabled,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
        )
        OutlinedTextField(
            value = dataUiState.verso,
            onValueChange = { onValueChange(dataUiState.copy(verso = it)) },
            label = { Text("Verso") },
            modifier = Modifier
                .fillMaxWidth()
                //.weight(1f)
                .align(Alignment.CenterHorizontally),
            enabled = enabled,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
        )
        Row(modifier = modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
            Text(text = saveState.message
            )
        }


    }

}

@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    LearnWithPierreTheme() {
        SaveBody(
            dataUiState = DataUiState(
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

