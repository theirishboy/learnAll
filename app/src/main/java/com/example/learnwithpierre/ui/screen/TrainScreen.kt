package com.example.learnwithpierre.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import java.util.Date

object TrainDestination : NavigationDestination {
    override val route = "trainScreen"
    override val titleRes: Int = R.string.app_name
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

fun TrainScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TrainViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{
    val currentQuestion = viewModel.currentQuestion
    val currentProgress = viewModel.trainUiScore.toFloat()
    Scaffold(
        topBar = {
            LearnAllTopAppBar(
                title = "Train",
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack,
            )
        } , content = {

            TrainBody(
                currentQuestion = currentQuestion,
                onCheckData = { viewModel.compareData() },
                trainUiState = viewModel.trainUiState,
                onValueChange = {
                        updatedUiState -> viewModel.updateUiState(updatedUiState) },
                progressFactor = currentProgress,
                modifier = modifier.padding(top = 50.dp)
            )
        })




}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainBody(
    currentQuestion: Data,
    trainUiState:TrainUiState,
    onValueChange: (TrainUiState) -> Unit,
    onCheckData: () -> Unit,
    progressFactor: Float,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,


    ) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressFactor,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        LinearProgressIndicator(
            modifier = Modifier
                .semantics(mergeDescendants = true) {}
                .padding(7.dp)
                .fillMaxWidth(),
            progress = animatedProgress,
        )
        Column(Modifier.weight(0.3f)) {

            Text(text = " A quoi correspond  ? :",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ))

            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),

                ) {
                Text(
                    text = currentQuestion.recto,
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                )
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = " Votre réponse :",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),


                ) {
                Column  (
                    modifier = Modifier.padding(16.dp)
                ) {
                    BasicTextField(
                        value = trainUiState.answer,
                        onValueChange = { onValueChange(trainUiState.copy(answer = it)) },
                        singleLine = false,
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )



                }
            }
            Button(
                onClick = onCheckData,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Valider")
            }

        }




    }




}

@Preview(showBackground = true)
@Composable
private fun TrainBodyPreview() {

    val currentQuestion =Data(1,"salut","connard",true,"",0, Date("01/02/2023"))
    val trainUiState = TrainUiState(answer = "J'aime le jambon d'auvergne et les phrases longues pour tester que tout se passe bien")

    TrainBody(
        currentQuestion = currentQuestion,
        trainUiState = trainUiState,
        onValueChange = {},
        onCheckData = {},
        progressFactor = 0.5f,
        modifier = Modifier,
        enabled = true
    )
}

@Preview
@Composable
private fun TrainScreenPreview(){

    val currentQuestion =Data(1,"salut","connard",true,"",0, Date("01/02/2023"))
    val trainUiState = TrainUiState(answer = "J'aime le jambon d'auvergne et les phrases longues pour tester que tout se passe bien")

    Scaffold(
        topBar = {
            LearnAllTopAppBar(
                title = "Train",
                canNavigateBack = true,
                navigateUp = {},
            )
        } , content = {innerPadding->
            TrainBody(
                currentQuestion = currentQuestion,
                trainUiState = trainUiState,
                onValueChange = {},
                onCheckData = {},
                progressFactor = 0.5f,
                modifier = Modifier.padding(innerPadding),
                enabled = true
            )
        })
}