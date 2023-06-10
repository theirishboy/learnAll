package com.example.learnwithpierre.ui.manageData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.compose.md_theme_light_primary
import com.example.compose.md_theme_light_tertiary
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

    var saveUiState by mutableStateOf(SaveState.NOTSHOW)

    /**
     * Updates the [dataUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newDataUiState: DataUiState) {
        if(saveUiState != SaveState.NOTSHOW) saveUiState = SaveState.NOTSHOW
        dataUiState = newDataUiState.copy( actionEnabled = newDataUiState.isValid())
    }
    suspend fun saveData() {
        if (dataUiState.isValid()) {
            dataRepository.insertData(dataUiState.toData())
            saveUiState = SaveState.SHOWSUCCESS
            dataUiState = DataUiState()
        }
    }

}
enum class SaveState(val message: String){
    NOTSHOW(""),SHOWSUCCESS("la sauvegarde est un succès"),SHOWFAILURE("la sauvegarde est un échec");

}