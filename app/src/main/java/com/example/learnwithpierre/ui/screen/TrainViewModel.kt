package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.md_theme_light_primary
import com.example.compose.md_theme_light_tertiary
import com.example.learnwithpierre.dao.Card
import com.example.learnwithpierre.dao.CardRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TrainViewModel(private val cardRepository: CardRepository) : ViewModel() {

    var trainUiState by mutableStateOf(TrainUiState(answer = ""))
        private set
    var trainUiScore by mutableStateOf(0.0)
        private set
    var showAnswerPopUp by mutableStateOf(AnswerState.NOTSHOW)
        private set

    var size : Int = 1
    var errorCard : Card = Card(0,"Please enter data in your db","vide",false,"animal",1, LocalDateTime.now())
    var currentQuestion by mutableStateOf(Card(0,"demo","vide",false,"animal",1, LocalDateTime.now()))

    init {
        viewModelScope.launch {
          trainUiState  = cardRepository.getRandomCard().map { TrainUiState(it as MutableList<Card>, answer = "") }.filterNotNull().first()
         try {
             currentQuestion =  trainUiState.cardList.last()
             size = trainUiState.cardList.size
         }catch (e : Exception){
                currentQuestion = errorCard
                trainUiState.cardList.add(errorCard)
            }

        }

    }

    fun compareData() {
        viewModelScope.launch {
            if(trainUiState.answer == trainUiState.cardList.last().verso ){
                showAnswerPopUp = AnswerState.TRUE
            }else{
                showAnswerPopUp = AnswerState.FALSE
            }
        }


    }
   fun updateUiState(trainUiState: TrainUiState) {
        this.trainUiState = TrainUiState(cardList =  trainUiState.cardList, answer = trainUiState.answer)
    }

    fun nextQuestion() {
        showAnswerPopUp = AnswerState.NOTSHOW
        trainUiScore += 1f/size
        trainUiState.answer = ""
        if(trainUiScore < 1f) {
            trainUiState.cardList.removeLast()
            currentQuestion = trainUiState.cardList.last()
        }

    }
}

/**
 * Ui State for trainScreen
 */
data class TrainUiState(var cardList: MutableList<Card> = arrayListOf(), var answer: String)

enum class AnswerState(val message: String, val color: Color){
    NOTSHOW("NOTSHOW",md_theme_light_tertiary),TRUE("vrai",md_theme_light_primary),FALSE("fausse",md_theme_light_tertiary);

}
