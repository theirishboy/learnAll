package com.example.learnwithpierre.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

object ShowAllDataScreenDestination : NavigationDestination {
    override val route = "ShowAllDataScreen"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun ShowAllCardScreen(
    navigateToTraining: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    viewModel: ShowAllCardsScreenViewModel = viewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),


    ){
    val allDataUiState = viewModel.showAllCardsUiState
    val items = listOf("Save","Train","Cards")
    val icons = listOf(R.drawable.baseline_download_24,R.drawable.baseline_model_training_24,R.drawable.baseline_dataset_24)
    val navigationScreens = listOf(navigateToHomeScreen,navigateToTraining, {})

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {Icon(painter = painterResource(id = icons[index]), contentDescription ="" )},
                        label = { androidx.compose.material3.Text(item) },
                        selected = index == 2,
                        onClick = navigationScreens[index]
                    )
                }
            }
        }
    ) { innerPadding ->
        ShowAllDataScreenBody(Modifier.padding(innerPadding),allDataUiState,  onCategoryChange = {
            coroutineScope.launch {
                viewModel.onCategoryChange(it)
            }
        })
    }
}
@Composable
fun ShowAllDataScreenBody(
    modifier: Modifier,
    allDataUiState: ShowAllCardsUiState,
    onCategoryChange: (String) -> Unit,
){
    Column(modifier = modifier) {
        OneLineData(recto = "recto", verso = "verso", category = "category", score = "score", title = true)
        FilterMenu(categories = allDataUiState.cardCategories, onCategoryChange = onCategoryChange)
        DisplayAllData(allDataUiState.filterFlashCardList)

    }

}
@Composable
fun DisplayAllData(listAllData : List<FlashCard>, modifier:Modifier = Modifier){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = listAllData, key = {it.cardId}) {
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
@OptIn(ExperimentalMaterial3Api::class)
private fun FilterMenu(categories : List<String>, onCategoryChange : (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var currentCategory by remember { mutableStateOf("") }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = currentCategory,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        currentCategory = item
                        onCategoryChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun OneLineData(recto: String, verso: String, category: String, score: String, title: Boolean = false){
    val column1Weight = .2f
    val column2Weight = .3f
    val column3Weight = .25f
    val column4Weight = .25f
    Row(modifier = Modifier.fillMaxWidth()) {
        TableCell(text = recto,column1Weight,title = title)
        TableCell(text = verso,column2Weight,title = title)
        TableCell(text = category,column3Weight,title = title)
        TableCell(text = score,column4Weight,title = title)

    }
}

@Preview
@Composable
fun OneLineDataPreview(){
    val flashCard = FlashCard(1,0,"2GM","1945",true,"Histoire",1, LocalDateTime.now())
    OneLineData(
        flashCard.recto,
        flashCard.verso,
        flashCard.category,
        flashCard.score.toString(),
        true
    )
}
@Preview
@Composable
fun DisplayAllDataPreview(){
    val flashCard = FlashCard(3,0,"2GM auqnd cest long ","1945",true,"Histoire",1, LocalDateTime.now())
    val flashCard2 = FlashCard(4052,0,"1GM","1918",true,"Histoire",1, LocalDateTime.now())
    val flashCard3 = FlashCard(1563,0,"start 2GM","1939",true,"Histoire",1, LocalDateTime.now())
    val table : List<FlashCard> = arrayListOf(flashCard,flashCard2,flashCard3)
    DisplayAllData(table)
}
@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(10.dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        fontSize = if(title) 19.sp else 15.sp,
        textAlign = alignment,

    )
}
//@Preview
//@Composable
//fun DisplayAllDataScreen(){
//    val data = Data(3,"2GM auqnd cest long ","1945",true,"Histoire",1, LocalDateTime.now())
//    val data2 = Data(4052,"1GM","1918",true,"Histoire",1, LocalDateTime.now())
//    val data3 = Data(1563,"start 2GM","1939",true,"Histoire",1, LocalDateTime.now())
//    val table : List<Data> = arrayListOf(data,data2,data3)
//    ShowAllDataScreenBody(Modifier.padding(2.dp))
//
//}
