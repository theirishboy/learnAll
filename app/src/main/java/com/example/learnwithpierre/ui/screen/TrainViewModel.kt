package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.dao.DataRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date

class TrainViewModel(private val dataRepository: DataRepository) : ViewModel() {



    var trainUiState by mutableStateOf(TrainUiState(answer = ""))
        private set

    var trainUiScore by mutableStateOf(0.0)
        private set

    var currentQuestion by mutableStateOf(Data(0,"demo","vide",false,"porn",1, Date("01/02/2022")))

    init {
        viewModelScope.launch {
          trainUiState  = dataRepository.getRandomData().map { TrainUiState(it as MutableList<Data>, answer = "") }.filterNotNull().first()
            currentQuestion = trainUiState.dataList.last()
        }
    }

    fun compareData() {
        viewModelScope.launch {
            if(trainUiState.answer!= trainUiState.dataList.last().recto ){
                println("null")
            }
            trainUiState.dataList.removeLast()
            currentQuestion = trainUiState.dataList.last()
            trainUiScore += 0.1
            trainUiState.answer = ""

            //dataRepository.updateData()
        }


    }
   fun updateUiState(trainUiState: TrainUiState) {
        this.trainUiState = TrainUiState(dataList =  trainUiState.dataList, answer = trainUiState.answer)
    }
}

/**
 * Ui State for trainScreen
 */
data class TrainUiState(val dataList: MutableList<Data> = arrayListOf(), var answer: String)


