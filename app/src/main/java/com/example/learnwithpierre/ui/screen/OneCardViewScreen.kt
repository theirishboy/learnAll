package com.example.learnwithpierre.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnwithpierre.R
import com.example.learnwithpierre.TopAppBarAddCardScreen
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.ui.navigation.NavigationDestination

object OneCardViewDestination : NavigationDestination {
    override val route = "CardView"
    override val titleRes: Int = R.string.app_name
    const val cardIdArg = "cardId"
    val routeWithArgs = "$route/{$cardIdArg}"
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OneCardViewScreen(
    dismissOnBackPress: () -> Unit,
    oneCardViewModel: OneCardViewModel = viewModel()
) {
    val oneCardUiState by oneCardViewModel.oneCardUiState.collectAsState()
    when (val state = oneCardUiState) {
        is OneCardUiState.Success -> OneCardScreen(
            state.flashCard,dismissOnBackPress,
            updateCard = { recto, verso -> oneCardViewModel.updateCard(recto,verso) }
        )
        is OneCardUiState.Error -> ErrorView(error = state.exception)
        is OneCardUiState.Loading -> LoadingView()
    }

}

@Composable
fun LoadingView() {
    Text(text = "loading")
}

@Composable
private fun OneCardScreen(
    flashCard: FlashCard?,
    dismissOnBackPress: () -> Unit,
    updateCard: (String,String) -> Unit

) {
    var recto by remember { mutableStateOf("") }
    var verso by remember { mutableStateOf("") }
    if (flashCard != null) {
        recto = flashCard.recto
        verso = flashCard.verso
    }
    val updateCardFunc = {updateCard(recto, verso); dismissOnBackPress() }
    Card(shape = RoundedCornerShape(15.dp),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopAppBarAddCardScreen(dismissOnBackPress, onSavePress = updateCardFunc, title = "Modify Card") }
        ) {

                paddingValue ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {
                Text(text = "Recto", modifier = Modifier.padding(start = 15.dp, top = 10.dp))
                Card(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.43f)
                        .padding(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    BasicTextField(
                        value = recto,
                        onValueChange = { recto = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textStyle = TextStyle(color = Color.Black),
                        decorationBox = { innerTextField ->
                            if (recto.isEmpty()) {
                                // Display a placeholder
                                Text("Tap here to start typing...")
                            }
                            innerTextField()
                        }
                    )
                }
                Text(text = "Verso", modifier = Modifier.padding(start = 15.dp, top = 10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f)
                        .padding(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    BasicTextField(
                        value = verso,
                        onValueChange = { verso = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textStyle = TextStyle(color = Color.Black),
                        decorationBox = { innerTextField ->
                            if (verso.isEmpty()) {
                                Text("Tap here to start typing...")
                            }
                            innerTextField()
                        }
                    )

                }
            }

        }

    }

}

@Preview
@Composable
fun AddCardDialogPreview(){
    val card = FlashCard(0,0,"hello","world",true,"Hello",5)
   // OneCardScreen(card, {})
}