package com.example.learnwithpierre.ui.manageData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.learnwithpierre.dao.DataRepository

/**
 * View Model to validate and insert data in the Room database.
 */
class DataEntryViewModel(private val dataRepository: DataRepository) : ViewModel() {

    /**
     * Holds current data ui state
     */
    var dataUiState by mutableStateOf(DataUiState())
        private set

    /**
     * Updates the [dataUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newDataUiState: DataUiState) {
        dataUiState = newDataUiState.copy( actionEnabled = newDataUiState.isValid())
    }
    suspend fun saveData() {
        if (dataUiState.isValid()) {
            dataRepository.insertData(dataUiState.toData())
        }
    }

}