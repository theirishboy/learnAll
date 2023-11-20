package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.CardRepository
import com.example.learnwithpierre.ui.manageData.CardUiState
import com.example.learnwithpierre.ui.manageData.isValid
import com.example.learnwithpierre.ui.manageData.toData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to validate and insert data in the Room database.
 */
class CardEntryViewModel(private val cardRepository: CardRepository) : ViewModel() {

    /**
     * Holds current data ui state
     */
    var cardUiState by mutableStateOf(CardUiState())
        private set

    var saveUiState by mutableStateOf(SaveState.NOTSHOW)

    val cardEntryUiState: StateFlow<cardEntryUiState> =
        cardRepository.getCategories().map { cardEntryUiState(it,it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = cardEntryUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    /**
     * Updates the [cardUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newCardUiState: CardUiState) {
        if(saveUiState != SaveState.NOTSHOW) saveUiState = SaveState.NOTSHOW
        cardUiState = newCardUiState.copy( actionEnabled = newCardUiState.isValid())
        cardEntryUiState.value.filterCategories = cardEntryUiState.value.categories.filter {s -> s.contains(cardUiState.category)}
    }
    suspend fun saveData() {
        if (cardUiState.isValid()) {
            cardRepository.insertCard(cardUiState.toData())
            saveUiState = SaveState.SHOWSUCCESS
            cardUiState = CardUiState()
        }
    }

}
enum class SaveState(val message: String){
    NOTSHOW(""),SHOWSUCCESS("la sauvegarde est un succès"),SHOWFAILURE("la sauvegarde est un échec");

}
data class cardEntryUiState(var categories : List<String> = listOf(), var filterCategories: List<String> = listOf())