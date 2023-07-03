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
import java.time.LocalDateTime

class ShowAllDataScreenViewModel(private val dataRepository: DataRepository) : ViewModel() {
    var showAllDataUiState by mutableStateOf(ShowAllDataUiState())
        private set
    init {
        viewModelScope.launch {
            showAllDataUiState  = dataRepository.getAllDataStream().map { ShowAllDataUiState(it) }.filterNotNull().first()
            //currentQuestion = trainUiState.dataList.last()
        }
    }
}
data class ShowAllDataUiState(val dataList: MutableList<Data> = arrayListOf())
