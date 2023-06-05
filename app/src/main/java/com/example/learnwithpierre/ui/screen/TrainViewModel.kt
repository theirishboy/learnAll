package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.dao.DataRepository
import com.example.learnwithpierre.ui.manageData.DataUiState
import com.example.learnwithpierre.ui.manageData.isValid
import com.example.learnwithpierre.ui.manageData.toDataUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TrainViewModel(dataRepository: DataRepository) : ViewModel() {
    val trainUiState: StateFlow<TrainUiState> =
        dataRepository.getAllDataStream().map { TrainUiState(it) }
        .stateIn(
                scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TrainUiState()
            )
    var dataUiState by mutableStateOf(DataUiState())
        private set

    /*fun updateUiState(listData : MutableList<Data>){
        dataUiState = listData.last().toDataUiState()
        listData.removeLast()
    }*/
    /*fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }*/


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    fun compareData(newDataUiState: DataUiState,answer : String) {
        if(newDataUiState.verso != answer){
            println("gg")
        }

    }
}

/**
 * Ui State for trainScreen
 */
data class TrainUiState(val dataList: List<Data> = listOf())

data class DataDetails(val id : Int = 0,
                       val recto: String = "",
                       val verso: String = "",

)