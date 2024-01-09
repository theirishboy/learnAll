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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TrainViewModel @Inject constructor(private val flashCardRepository: FlashCardRepository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val deckId: Long = checkNotNull(savedStateHandle[TrainDestination.deckIdArg]).toString().toLong()
    var trainUiState by mutableStateOf(TrainUiState(answer = ""))
        private set
    var trainUiScore by mutableStateOf(0.0)
        private set

    private var size : Int = 1
    private var errorFlashCard : FlashCard = FlashCard(0,0,"Please enter a new card in your db","vide",false,"animal",1, LocalDateTime.now())
    var currentQuestion by mutableStateOf(FlashCard(0,0,"demo","vide",false,"animal",1, LocalDateTime.now()))

    init {
        if (deckId.compareTo(0) == 0) {
            viewModelScope.launch {
                trainUiState  = flashCardRepository.getRandomCard().map { TrainUiState(it, answer = "") }.filterNotNull().first()
                try {
                    currentQuestion =  trainUiState.flashCardList.last()
                    size = trainUiState.flashCardList.size
                }catch (e : Exception){
                    currentQuestion = errorFlashCard
                    trainUiState.flashCardList.add(errorFlashCard)
                }

            }        }else{
            viewModelScope.launch {
                trainUiState  = flashCardRepository.getCardByDeckId(deckId).map { TrainUiState(it, answer = "") }.filterNotNull().first()
                try {
                    currentQuestion =  trainUiState.flashCardList.last()
                    size = trainUiState.flashCardList.size
                }catch (e : Exception){
                    currentQuestion = errorFlashCard
                    trainUiState.flashCardList.add(errorFlashCard)
                }

            }
        }


    }
   fun updateUiState(trainUiState: TrainUiState) {
        this.trainUiState = TrainUiState(flashCardList =  trainUiState.flashCardList, answer = trainUiState.answer)
    }
    fun updateFlashCard(flashCard: FlashCard, answer: AnswerQuality){
        val dateModification = LocalDateTime.now()

        val score = calculateNewScore(answer, flashCard)

        var dateTraining = flashCard.dateTraining
        dateTraining = calculateNewDateTraining(score, dateTraining)

        val isRecto = !flashCard.isRecto

        val newFlashCard = FlashCard(flashCard.cardId,
            flashCard.deckId,
            flashCard.recto,
            flashCard.verso,
            isRecto,
            flashCard.category,
            score,
            flashCard.dateCreation,
            dateModification,
            dateTraining)
        viewModelScope.launch(Dispatchers.IO) {
            flashCardRepository.updateCard(flashCard = newFlashCard)
        }
    }

    private fun calculateNewScore(
        answer: AnswerQuality,
        flashCard: FlashCard
    ): Int {
        var score = flashCard.score
        when (answer) {
            AnswerQuality.BAD -> score = 0
            AnswerQuality.FAIR -> score = if (flashCard.score > 0) {
                flashCard.score - 1
            } else 0

            AnswerQuality.FINE -> if (score > 2) score = 2
            AnswerQuality.PERFECT -> score += 1
        }
        return score
    }

    private fun calculateNewDateTraining(
        score: Int,
        dateTraining: LocalDateTime
    ): LocalDateTime {
        var dateTraining1 = dateTraining
        when (score) {
            0 -> dateTraining1 = LocalDateTime.now()
            1 -> dateTraining1 = LocalDateTime.now().plusDays(1)
            2 -> dateTraining1 = LocalDateTime.now().plusDays(3)
            3 -> dateTraining1 = LocalDateTime.now().plusDays(7)
            4 -> dateTraining1 = LocalDateTime.now().plusDays(14)
            5 -> dateTraining1 = LocalDateTime.now().plusDays(30)
            6 -> dateTraining1 = LocalDateTime.now().plusDays(90)
            7 -> dateTraining1 = LocalDateTime.now().plusDays(180)
        }
        return dateTraining1
    }

    fun nextQuestion() {
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
enum class AnswerQuality {
    BAD,
    FAIR,
    FINE,
    PERFECT
}
