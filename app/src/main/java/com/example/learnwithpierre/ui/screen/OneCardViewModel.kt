package com.example.learnwithpierre.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.dao.FlashCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneCardViewModel @Inject constructor(private val cardRepository: FlashCardRepository, savedStateHandle: SavedStateHandle) : ViewModel() {
    private val cardId: Long = checkNotNull(savedStateHandle[OneCardViewDestination.cardIdArg]).toString().toLong()

    private val _oneCardUiState = MutableStateFlow<OneCardUiState>(OneCardUiState.Loading("Loading"))

    val oneCardUiState: StateFlow<OneCardUiState> = _oneCardUiState.asStateFlow()
    init {
        viewModelScope.launch{
            cardRepository.getCardStream(cardId).collect{
                    card -> _oneCardUiState.value = OneCardUiState.Success(card)
            }
        }
    }
    fun updateCard(recto: String, verso: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if the current state has a FlashCard
            val currentCard = when (val currentState = _oneCardUiState.value) {
                is OneCardUiState.Success -> currentState.flashCard
                else -> null
            }

            if (currentCard != null) {
                // Update the flashCard with new values
                val updatedCard = currentCard.copy(recto = recto, verso = verso)

                try {
                    // Call the repository to update the card
                    cardRepository.updateCard(updatedCard)
                    // Optionally, update the UI state to reflect the successful update
                    _oneCardUiState.value = OneCardUiState.Success(updatedCard)
                } catch (e: Exception) {
                    // Handle any errors during the update process
                    _oneCardUiState.value = OneCardUiState.Error(e)
                }
            }
        }
    }

}
sealed class OneCardUiState {
    data class Success(val flashCard: FlashCard?): OneCardUiState()
    data class Error(val exception: Throwable): OneCardUiState()
    data class Loading(val message: String): OneCardUiState()
}