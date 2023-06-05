package com.example.learnwithpierre.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme
import java.util.Date

object TrainDestination : NavigationDestination {
    override val route = "trainScreen"
    override val titleRes: Int = R.string.app_name
}
// Custom saver for CustomClass
object DataSaver : Saver<Data, Bundle> {
    override fun SaverScope.save(value: Data): Bundle {
        val bundle = Bundle()
        bundle.putInt("id", value.id)
        bundle.putString("recto", value.recto)
        bundle.putString("verso", value.verso)
        bundle.putBoolean("isRecto", value.isRecto)
        bundle.putString("category", value.category)
        bundle.putInt("score", value.score)
        bundle.putLong("dateModification", value.dateModification.time)
        return bundle
    }

    override fun restore(value: Bundle): Data {
        return Data(
            id = value.getInt("id"),
            recto = value.getString("recto") ?: "",
            verso = value.getString("verso") ?: "",
            isRecto = value.getBoolean("isRecto"),
            category = value.getString("category") ?: "",
            score = value.getInt("score"),
            dateModification = Date(value.getLong("dateModification"))
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TrainScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TrainViewModel = viewModel(factory = AppViewModelProvider.Factory))
{
    val trainUiState by viewModel.trainUiState.collectAsState()



    Scaffold(topBar = {
        LearnAllTopAppBar(
            title = "Train",
            canNavigateBack = canNavigateBack,
            navigateUp = navigateBack
        )
    }
    ){
        if(trainUiState.dataList.isNotEmpty()){

            var currentQuestion : Data? by remember(DataSaver){ mutableStateOf(null) }
            var count by remember { mutableStateOf(0) }
            val ontamere : (Int) -> Unit = {value -> count += value}
            LaunchedEffect(count){
                currentQuestion = trainUiState.dataList[count]

            }
            currentQuestion?.let { it1 ->
                TrainBody(
                    it1,ontamere)
            }

        }else{
            Text(text = "loading...")
        }
    }


}

@Composable
fun TrainBody(
    currentQuestion: Data,
    //dataList : List<Data>,
   // trainUiState: StateFlow<TrainUiState>,
    //onValueChange: (DataUiState) -> Unit,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),

            ) {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFF518A0),
                                Color(0xFFB232BD)
                            )
                        )
                    )
                    .padding(all = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentQuestion.recto,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
        Card(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),

            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center, modifier = modifier.weight(1f)
            ) {
                //Spacer(modifier = Modifier.height(100.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {  },
                    label = { "Recto" },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    // shape = false
                    // singleLine = true,

                )

            }
        }
        Card(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.2f),
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),

            )
        {
            Button(
                onClick = {onClick.invoke(1)},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            //   enabled = dataUiState.actionEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("test")
            }


        }
    }




}
@Composable
fun CounterButton(onClick: (Int) -> Unit) {
    Button(onClick = { onClick.invoke(1) }) {
        Text("Increment")
    }
}
/*@Preview(showBackground = true)
@Composable
private fun TrainScreenPreview() {
    LearnWithPierreTheme() {
        TrainBody(
            Data(1,"salut","connard",true,"",0, Date("01/02/2023"))
         /*   dataUiState = DataUiState(
                recto = "Item name",
                verso = "10.00",
            ),
            onValueChange = {},
            onSaveClick = {}
        */)
    }
}*/
