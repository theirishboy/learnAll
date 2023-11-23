package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.CardRepository
import com.example.learnwithpierre.dao.Deck
import com.example.learnwithpierre.dao.DeckRepository
import com.example.learnwithpierre.dao.User
import com.example.learnwithpierre.dao.UserRepository
import com.example.learnwithpierre.ui.manageData.CardUiState
import com.example.learnwithpierre.ui.manageData.isValid
import com.example.learnwithpierre.ui.manageData.toData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

/**
 * View Model to validate and insert data in the Room database.
 */
class CardEntryViewModel(private val cardRepository: CardRepository,
                         private val userRepository: UserRepository,
                         private val deckRepository: DeckRepository
) : ViewModel() {

    /**
     * Holds current data ui state
     */
    init {
        runBlocking(Dispatchers.IO) {
            // Insert user
            val newUser = User(0, "Test", "Test", LocalDateTime.now(), LocalDateTime.now())
            val generatedUserId = userRepository.insertUser(newUser)

            // Use the generated user ID for the deck
            val newDeck = Deck(0, 1, "demo", "", LocalDateTime.now(), LocalDateTime.now())
            deckRepository.insertDeck(newDeck)
        }
    }
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