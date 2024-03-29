package com.example.learnwithpierre.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.learnwithpierre.dao.FlashCardRepository
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Deck
import com.example.learnwithpierre.dao.DeckRepository
import com.example.learnwithpierre.dao.FlashCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class OneDeckViewModel @Inject constructor(private val deckRepository: DeckRepository, private val cardRepository: FlashCardRepository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val deckId: Long = checkNotNull(savedStateHandle[OneDeckViewDestination.deckIdArg]).toString().toLong()

    private val _oneDeckUiState = MutableStateFlow(OneDeckUiState.Success(Deck(0,0,"Fail",""),arrayListOf()))

    val oneDeckUiState: StateFlow<OneDeckUiState> = _oneDeckUiState.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO){
            val deck : Deck = deckRepository.getDeck(deckId = deckId)
            cardRepository.getCardByDeckId(deckId).collect{
                card -> _oneDeckUiState.value = OneDeckUiState.Success(deck,card)
            }

        }
    }
    fun addOneCardToDeck(recto: String, verso: String){
            viewModelScope.launch(Dispatchers.IO){
                val newCard = FlashCard(0, deckId, recto, verso,true,"undefined",0, LocalDateTime.now(), LocalDateTime.now())
                cardRepository.insertCard(newCard)
                deckRepository.updateDateModificationByDeckById(deckId, LocalDateTime.now())
            }

        }
    fun getDeckId() : Long{
        return deckId
    }

    fun deleteDeck() {
        viewModelScope.launch(Dispatchers.IO) {
            deckRepository.deleteDeckById(getDeckId())
        }

    }

    fun deleteOneCard(card: FlashCard) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.deleteCard(card)
        }


    }
}
sealed class OneDeckUiState {
    data class Success(val deck: Deck, val flashCards: MutableList<FlashCard>): OneDeckUiState()
    data class Error(val exception: Throwable): OneDeckUiState()
}

