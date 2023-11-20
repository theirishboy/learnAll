package com.example.learnwithpierre.ui.manageData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.CardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to validate and insert data in the Room database.
 */
class DataEntryViewModel(private val cardRepository: CardRepository) : ViewModel() {

    /**
     * Holds current data ui state
     */
    var dataUiState by mutableStateOf(DataUiState())
        private set

    var saveUiState by mutableStateOf(SaveState.NOTSHOW)

    val dataEntryUiState: StateFlow<DataEntryUiState> =
        cardRepository.getCategories().map { DataEntryUiState(it,it) }
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
        dataEntryUiState.value.filterCategories = dataEntryUiState.value.categories.filter {s -> s.contains(dataUiState.category)}
    }
    suspend fun saveData() {
        if (dataUiState.isValid()) {
            cardRepository.insertCard(dataUiState.toData())
            saveUiState = SaveState.SHOWSUCCESS
            dataUiState = DataUiState()
        }
    }

}
enum class SaveState(val message: String){
    NOTSHOW(""),SHOWSUCCESS("la sauvegarde est un succès"),SHOWFAILURE("la sauvegarde est un échec");

}
data class DataEntryUiState(var categories : List<String> = listOf(), var filterCategories: List<String> = listOf())