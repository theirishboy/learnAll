package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.ui.theme.md_theme_light_primary
import com.example.learnwithpierre.ui.theme.md_theme_light_tertiary
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.dao.FlashCardRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TrainViewModel(private val flashCardRepository: FlashCardRepository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val deckId: Long = checkNotNull(savedStateHandle[TrainDestination.deckIdArg]).toString().toLong()
    var trainUiState by mutableStateOf(TrainUiState(answer = ""))
        private set
    var trainUiScore by mutableStateOf(0.0)
        private set
    var showAnswerPopUp by mutableStateOf(AnswerState.NOTSHOW)
        private set

    var size : Int = 1
    var errorFlashCard : FlashCard = FlashCard(0,0,"Please enter a new card in your db","vide",false,"animal",1, LocalDateTime.now())
    var currentQuestion by mutableStateOf(FlashCard(0,0,"demo","vide",false,"animal",1, LocalDateTime.now()))

    init {
        viewModelScope.launch {
          trainUiState  = flashCardRepository.getCardByDeckId(deckId).map { TrainUiState(it as MutableList<FlashCard>, answer = "") }.filterNotNull().first()
         try {
             currentQuestion =  trainUiState.flashCardList.last()
             size = trainUiState.flashCardList.size
         }catch (e : Exception){
                currentQuestion = errorFlashCard
                trainUiState.flashCardList.add(errorFlashCard)
            }

        }

    }

    fun compareCards() {
        viewModelScope.launch {
            if(trainUiState.answer == trainUiState.flashCardList.last().verso ){
                showAnswerPopUp = AnswerState.TRUE
            }else{
                showAnswerPopUp = AnswerState.FALSE
            }
        }


    }
   fun updateUiState(trainUiState: TrainUiState) {
        this.trainUiState = TrainUiState(flashCardList =  trainUiState.flashCardList, answer = trainUiState.answer)
    }

    fun nextQuestion() {
        showAnswerPopUp = AnswerState.NOTSHOW
        trainUiScore += 1f/size
        trainUiState.answer = ""
        if(trainUiScore < 1f) {
            trainUiState.flashCardList.removeLast()
            currentQuestion = trainUiState.flashCardList.last()
        }

    }
}

/**
 * Ui State for trainScreen
 */
data class TrainUiState(var flashCardList: MutableList<FlashCard> = arrayListOf(), var answer: String)

enum class AnswerState(val message: String, val color: Color){
    NOTSHOW("NOTSHOW", md_theme_light_tertiary),TRUE("vrai", md_theme_light_primary),FALSE("fausse",
        md_theme_light_tertiary
    );

}
