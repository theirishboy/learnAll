package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.md_theme_light_primary
import com.example.compose.md_theme_light_tertiary
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.dao.DataRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Date

class TrainViewModel(private val dataRepository: DataRepository) : ViewModel() {

    var trainUiState by mutableStateOf(TrainUiState(answer = ""))
        private set
    var trainUiScore by mutableStateOf(0.0)
        private set
    var showAnswerPopUp by mutableStateOf(AnswerState.NOTSHOW)
        private set

    var size : Int = 1
    var errorData : Data = Data(0,"Please enter data in your db","vide",false,"animal",1, LocalDateTime.now())
    var currentQuestion by mutableStateOf(Data(0,"demo","vide",false,"animal",1, LocalDateTime.now()))

    init {
        viewModelScope.launch {
          trainUiState  = dataRepository.getRandomData().map { TrainUiState(it as MutableList<Data>, answer = "") }.filterNotNull().first()
         try {
             currentQuestion =  trainUiState.dataList.last()
             size = trainUiState.dataList.size
         }catch (e : Exception){
                currentQuestion = errorData
                trainUiState.dataList.add(errorData)
            }

        }

    }

    fun compareData() {
        viewModelScope.launch {
            if(trainUiState.answer == trainUiState.dataList.last().verso ){
                showAnswerPopUp = AnswerState.TRUE
            }else{
                showAnswerPopUp = AnswerState.FALSE
            }
        }


    }
   fun updateUiState(trainUiState: TrainUiState) {
        this.trainUiState = TrainUiState(dataList =  trainUiState.dataList, answer = trainUiState.answer)
    }

    fun nextQuestion() {
        showAnswerPopUp = AnswerState.NOTSHOW
        trainUiScore += 1f/size
        trainUiState.answer = ""
        if(trainUiScore < 1f) {
            trainUiState.dataList.removeLast()
            currentQuestion = trainUiState.dataList.last()
        }

    }
}

/**
 * Ui State for trainScreen
 */
data class TrainUiState(var dataList: MutableList<Data> = arrayListOf(), var answer: String)

enum class AnswerState(val message: String, val color: Color){
    NOTSHOW("NOTSHOW",md_theme_light_tertiary),TRUE("vrai",md_theme_light_primary),FALSE("fausse",md_theme_light_tertiary);

}
