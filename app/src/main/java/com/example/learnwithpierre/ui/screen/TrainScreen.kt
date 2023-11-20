package com.example.learnwithpierre.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.md_theme_light_onPrimary
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.Card
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import java.time.LocalDateTime

object TrainDestination : NavigationDestination {
    override val route = "trainScreen"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun TrainScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToHome: () -> Unit,

    viewModel: TrainViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{
    val currentQuestion = viewModel.currentQuestion
    val currentProgress = viewModel.trainUiScore.toFloat()
    val localFocusManager = LocalFocusManager.current
    if(currentProgress >= 1f){
        navigateBack()
    }
    Scaffold(
        topBar = {
            LearnAllTopAppBar(
                title = "Train",
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack,
            )
        } , content = {innerPadding ->

            TrainBody(
                currentQuestion = currentQuestion,
                onCheckCard = { viewModel.compareCards() },
                trainUiState = viewModel.trainUiState,
                onValueChange = {
                        updatedUiState -> viewModel.updateUiState(updatedUiState) },
                progressFactor = currentProgress,
                modifier = modifier.padding(innerPadding).clickable { localFocusManager.clearFocus()  }
            )
        })
    val popUpControl = viewModel.showAnswerPopUp
    if (popUpControl != AnswerState.NOTSHOW) {
        BottomPopUp(modifier = modifier, OnContinue = { viewModel.nextQuestion() }, popUpControl)
    }



}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomPopUp(modifier: Modifier, OnContinue: () -> Unit,answerState: AnswerState) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, 700),
        onDismissRequest = {},
        PopupProperties(clippingEnabled = true, usePlatformDefaultWidth = false, focusable = true)
    ) {
        Column(
            modifier
                .background(md_theme_light_onPrimary)
                .fillMaxWidth()
                .fillMaxHeight(0.2F), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "Votre réponse est " + answerState.message)
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = answerState.color),
                onClick = OnContinue
            ) {
                Text(text = "Continuer")
            }

        }
    }
}

@Composable
fun TrainBody(
    currentQuestion: Card,
    trainUiState:TrainUiState,
    onValueChange: (TrainUiState) -> Unit,
    onCheckCard: () -> Unit,
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
                onClick = onCheckCard,
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

    val currentQuestion =Card(1,"salut","connard",true,"",0, LocalDateTime.now())
    val trainUiState = TrainUiState(answer = "J'aime le jambon d'auvergne et les phrases longues pour tester que tout se passe bien")

    TrainBody(
        currentQuestion = currentQuestion,
        trainUiState = trainUiState,
        onValueChange = {},
        onCheckCard = {},
        progressFactor = 0.5f,
        modifier = Modifier,
        enabled = true
    )
}

@Preview
@Composable
private fun TrainScreenPreview(){

    val currentQuestion =Card(1,"salut","connard",true,"",0, LocalDateTime.now())
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
                onCheckCard = {},
                progressFactor = 0.5f,
                modifier = Modifier.padding(innerPadding),
                enabled = true
            )
        })
}

