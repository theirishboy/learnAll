package com.example.learnwithpierre.ui.manageData

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
import com.example.learnwithpierre.ui.screen.HomeUiState
import com.example.learnwithpierre.ui.screen.TrainUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    val dataEntryUiState: StateFlow<DataEntryUiState> =
        dataRepository.getCategories().map { DataEntryUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DataEntryUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
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
data class DataEntryUiState(var categories : List<String> = listOf())