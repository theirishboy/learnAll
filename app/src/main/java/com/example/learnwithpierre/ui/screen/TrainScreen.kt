package com.example.learnwithpierre.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.DeckViewTopAppBar
import com.example.learnwithpierre.LearnAllTopAppBar
import com.example.learnwithpierre.R
import com.example.learnwithpierre.dao.FlashCard
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
    trainViewModel: TrainViewModel = viewModel()
) {
    val currentQuestion = trainViewModel.currentQuestion
    val currentProgress = trainViewModel.trainUiScore.toFloat()
    if (currentProgress >= 1f) {
        navigateBack()
    }
    Scaffold(
        topBar = {
            LearnAllTopAppBar(
                title = "Train",
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
            )
        }, content = { innerPadding ->

            TrainBody(
                currentQuestion = currentQuestion,
                trainUiState = trainViewModel.trainUiState,
                onValueChange = { updatedUiState -> trainViewModel.updateUiState(updatedUiState) },
                progressFactor = currentProgress,
                updateScore = { flashCard, answerQuality ->
                    trainViewModel.updateFlashCard(
                        flashCard,
                        answerQuality
                    )
                },
                nextQuestion = { trainViewModel.nextQuestion() },
                modifier = modifier
                    .padding(innerPadding)
            )
        })
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
    var hasAnswer by remember { mutableStateOf(false) }
    var topTitle by remember { mutableStateOf("what does this match witch ?") }
    var contentFirstCard by remember { mutableStateOf("") }
    var canChangeAnswer by remember { mutableStateOf(true) }
    contentFirstCard =  if (currentQuestion.isRecto )currentQuestion.recto else currentQuestion.verso
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
                    text = contentFirstCard,
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                )
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "What are your thoughts ? :",
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
                        enabled = canChangeAnswer,
                        modifier = Modifier.fillMaxWidth()
                    )



                }
            }




        }
        if(!hasAnswer){
            topTitle = "Find what match with"
            canChangeAnswer = true
            Row{
                Button(
                    onClick = {hasAnswer = true},
                    modifier = Modifier.fillMaxWidth(),
                    shape =  RoundedCornerShape(15.dp),
                ) {
                    Text("Validate")
                }
            }
        }else{
            topTitle = "It match with"
            contentFirstCard = if (currentQuestion.isRecto )currentQuestion.verso else currentQuestion.recto
            canChangeAnswer = false
            ShowAnswer(modifier, updateScore, currentQuestion, nextQuestion) { hasAnswer = false }
        }



    }






}

@Composable
private fun ShowAnswer(
    modifier: Modifier,
    updateScore: (FlashCard, AnswerQuality) -> Unit,
    currentQuestion: FlashCard,
    nextQuestion: () -> Unit,
    onClick: () -> Unit
) {
    Row(modifier.fillMaxWidth()) {
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.BAD); onClick(); nextQuestion(); },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color(0xFFF4B6B6)),
            shape = RectangleShape
        ) {
            Text("BAD")
        }
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.FAIR); onClick(); nextQuestion() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color(0xFFFFCC99)),
            shape = RectangleShape

        ) {
            Text("FAIR")
        }
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.FINE);  onClick();nextQuestion() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color(0xFFFFF5BA)),
            shape = RectangleShape

        ) {
            Text("FINE")
        }
        TextButton(
            onClick = { updateScore(currentQuestion, AnswerQuality.PERFECT);  onClick(); nextQuestion() },
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
            LearnAllTopAppBar(
                title = "Train",
                canNavigateBack = true,
                navigateBack = {},
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

