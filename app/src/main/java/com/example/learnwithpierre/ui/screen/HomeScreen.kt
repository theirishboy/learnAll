package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    navigateBack: () -> Unit,
    navigateToTraining: () -> Unit,
    viewModel: DataEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Save","Train","Data")
    val icons = listOf(R.drawable.baseline_download_24,R.drawable.baseline_model_training_24,R.drawable.baseline_dataset_24)
    val saveState = viewModel.saveUiState
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {R.drawable.baseline_download_24},
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = navigateToTraining
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
                    navigateBack()

                }
            },
            saveState = saveState,
            modifier = modifier.padding(innerPadding),
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


    ){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,modifier = modifier
                .fillMaxWidth()
                .weight(0.8f)) {
            Text(text = "Sauvegarder vos données")
        }
        Row(modifier = modifier.weight(1f)){
            //Spacer(modifier = Modifier.height(100.dp))
            SaveForm(dataUiState = dataUiState, onValueChange = onValueChange, saveState = saveState)

        }


        Button(
            onClick = onSaveClick,
            enabled = dataUiState.actionEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }




    }

}
@Composable
fun SaveForm( dataUiState: DataUiState,
              modifier: Modifier = Modifier,
              onValueChange: (DataUiState) -> Unit = {},
              enabled: Boolean = true,
              saveState: SaveState){
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = dataUiState.category,
            onValueChange = { onValueChange(dataUiState.copy(category = it)) },
            label = { Text("Category") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = dataUiState.recto,
            onValueChange = { onValueChange(dataUiState.copy(recto = it)) },
            label = { Text("Recto") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = dataUiState.verso,
            onValueChange = { onValueChange(dataUiState.copy(verso = it)) },
            label = { Text("Verso") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            enabled = enabled,
            singleLine = true
        )
        Text(text = saveState.message)

    }

}

@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    LearnWithPierreTheme() {
        SaveBody(
            dataUiState = DataUiState(
                recto = "Item name",
                verso = "10.00",
            ),
            onValueChange = {},
            onSaveClick = {},
            saveState = SaveState.SHOWSUCCESS
        )
    }
}
