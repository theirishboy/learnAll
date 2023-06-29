package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import java.time.LocalDateTime

object ShowAllDataScreenRoute : NavigationDestination {
    override val route = "ShowAllDataScreen"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun ShowAllDataScreen(
    navigateToTraining: () -> Unit,
    viewModel: ShowAllDataScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)

){
    val table = viewModel.showAllDataUiState.dataList
    val items = listOf("Save","Train","Data")
    val icons = listOf(R.drawable.baseline_download_24,R.drawable.baseline_model_training_24,R.drawable.baseline_dataset_24)
    val selectedItem by remember { mutableStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {Icon(painter = painterResource(id = icons[index]), contentDescription ="" )},
                        label = { androidx.compose.material3.Text(item) },
                        selected = selectedItem == index,
                        onClick = navigateToTraining
                    )
                }
            }
        }
    ) { innerPadding ->
        Column() {
            OneLineData(recto = "recto", verso = "Verso", category = "category", score = "score")
            DisplayAllData(table, modifier = Modifier.padding(innerPadding))
        }

    }
}

@Composable
fun DisplayAllData(listAllData : List<Data>, modifier:Modifier = Modifier){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {

        items(items = listAllData, key = {it.id}) {
            item-> OneLineData(
            recto = item.recto,
            verso = item.verso,
            category = item.category,
            score = item.score.toString(),
        )

        }
    }

}


@Composable
fun OneLineData(recto: String, verso: String, category: String, score: String){
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = recto)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = verso)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = category)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = score)
        }

    }
}

@Preview
@Composable
fun OneLineDataPreview(){
    val data = Data(1,"2GM","1945",true,"Histoire",1, LocalDateTime.now())
    OneLineData(
        data.recto,
        data.verso,
        data.category,
        data.score.toString(),
    )
}
@Preview
@Composable
fun DisplayAllDataPreview(){
    val data = Data(3,"2GM","1945",true,"Histoire",1, LocalDateTime.now())
    val data2 = Data(4052,"1GM","1918",true,"Histoire",1, LocalDateTime.now())
    val data3 = Data(1563,"start 2GM","1939",true,"Histoire",1, LocalDateTime.now())
    val table : List<Data> = arrayListOf(data,data2,data3)
    DisplayAllData(table)
}