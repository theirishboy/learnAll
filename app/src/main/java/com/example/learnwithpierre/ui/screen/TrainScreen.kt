package com.example.learnwithpierre.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.example.learnwithpierre.ui.theme.md_theme_light_onPrimary
import com.example.learnwithpierre.DeckViewTopAppBar
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.ui.AppViewModelProvider
import com.example.learnwithpierre.ui.navigation.NavigationDestination
import java.time.LocalDateTime

object TrainDestination : NavigationDestination {
    override val route = "trainScreen"
    override val titleRes: Int = R.string.app_name
    const val deckIdArg = "cardId"
    val routeWithArgs = "$route/{$deckIdArg}"}

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
    LocalFocusManager.current
    if(currentProgress >= 1f){
        navigateBack()
    }
    Scaffold(
        topBar = {
            LearnAllTopAppBar(
                title = "Train",
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
            )
        } , content = {innerPadding ->

            TrainBody(
                currentQuestion = currentQuestion,
                trainUiState = viewModel.trainUiState,
                onValueChange = {
                        updatedUiState -> viewModel.updateUiState(updatedUiState) },
                progressFactor = currentProgress,
                updateScore =  { flashCard, answerQuality -> viewModel.updateFlashCard(flashCard, answerQuality) },
                nextQuestion = { viewModel.nextQuestion() },
                modifier = modifier
                    .padding(innerPadding)
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
    currentQuestion: FlashCard,
    trainUiState: TrainUiState,
    onValueChange: (TrainUiState) -> Unit,
    progressFactor: Float,
    updateScore: (FlashCard, AnswerQuality) -> Unit,
    nextQuestion : () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressFactor,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )
    var hasAnswer by remember {
        mutableStateOf(false)
    }
    var topTitle by remember {
        mutableStateOf("A quoi correspond  ? :")
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LinearProgressIndicator(modifier = Modifier
            .semantics(mergeDescendants = true) {}
            .padding(7.dp)
            .fillMaxWidth(),
            progress = animatedProgress,)
        Column(Modifier.weight(0.6f)) {
            Text(text = topTitle,
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
                    .weight(0.9f),
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
            if(!hasAnswer){
                Row{
                    Button(
                        onClick = {hasAnswer = true},
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Valider")
                    }
                }
            }else{
                topTitle = "ça correspond à"
                ShowAnswer(modifier, updateScore, currentQuestion, nextQuestion)
            }



        }




    }




}

@Composable
private fun ShowAnswer(
    modifier: Modifier,
    updateScore: (FlashCard, AnswerQuality) -> Unit,
    currentQuestion: FlashCard,
    nextQuestion: () -> Unit
) {
    Row(modifier.fillMaxWidth()) {
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.BAD); nextQuestion() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color(0xFFF4B6B6)),
            shape = RectangleShape
        ) {
            Text("BAD")
        }
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.FAIR); nextQuestion() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color(0xFFFFCC99)),
            shape = RectangleShape

        ) {
            Text("FAIR")
        }
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.FINE); nextQuestion() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color(0xFFFFF5BA)),
            shape = RectangleShape

        ) {
            Text("FINE")
        }
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.PERFECT); nextQuestion() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color(0xFFA8D5BA)),
            shape = RectangleShape

        ) {
            Text("PERFECT")
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun TrainBodyPreview() {
//
//    val currentQuestion =FlashCard(1,0,"salut","connard",true,"",0, LocalDateTime.now())
//    val trainUiState = TrainUiState(answer = "J'aime le jambon d'auvergne et les phrases longues pour tester que tout se passe bien")
//
//    TrainBody(
//        currentQuestion = currentQuestion,
//        trainUiState = trainUiState,
//        onValueChange = {},
//        onCheckCard = {},
//        progressFactor = 0.5f,
//        modifier = Modifier,
//        enabled = true
//    )
//}

@Preview
@Composable
private fun TrainScreenPreview(){

    val currentQuestion =FlashCard(1,0,"salut","connard",true,"",0, LocalDateTime.now())
    val trainUiState = TrainUiState(answer = "J'aime le jambon d'auvergne et les phrases longues pour tester que tout se passe bien")

    Scaffold(
        topBar = {
            DeckViewTopAppBar(
                title = "Train",
                canNavigateBack = true,
                navigateBack = {},
                deleteDeck = {},
            )
        } , content = {innerPadding->
            TrainBody(
                currentQuestion = currentQuestion,
                trainUiState = trainUiState,
                onValueChange = {},
                progressFactor = 0.5f,
                updateScore = { a, b -> },
                nextQuestion = {},
                modifier = Modifier.padding(innerPadding)
            )
        })
}

