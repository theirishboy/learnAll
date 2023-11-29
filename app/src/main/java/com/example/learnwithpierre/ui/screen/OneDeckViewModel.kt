package com.example.learnwithpierre.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.learnwithpierre.dao.FlashCardRepository
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.FlashCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class OneDeckViewModel(private val cardRepository: FlashCardRepository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val deckId: Long = checkNotNull(savedStateHandle[OneDeckViewDestination.deckIdArg]).toString().toLong()

    private val _oneDeckUiState = MutableStateFlow(OneDeckUiState.Success(arrayListOf()))

    val oneDeckUiState: StateFlow<OneDeckUiState> = _oneDeckUiState.asStateFlow()

    init {
        viewModelScope.launch{
            val newCard = FlashCard(0, 1, "recto", "verso",true,"Yo",5, LocalDateTime.now(), LocalDateTime.now())
            cardRepository.insertCard(newCard)
            cardRepository.getCardByDeckId(deckId).collect{
                card -> _oneDeckUiState.value = OneDeckUiState.Success(card)
            }
        }
    }
}
sealed class OneDeckUiState {
    data class Success(val flashCards: MutableList<FlashCard>): OneDeckUiState()
    data class Error(val exception: Throwable): OneDeckUiState()
}

