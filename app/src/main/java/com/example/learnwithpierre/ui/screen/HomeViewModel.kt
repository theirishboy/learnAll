package com.example.learnwithpierre.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Card
import com.example.learnwithpierre.dao.CardRepository
import com.example.learnwithpierre.dao.Deck
import com.example.learnwithpierre.dao.DeckDao
import com.example.learnwithpierre.dao.DeckRepository
import com.example.learnwithpierre.ui.manageData.CardUiState
import com.example.learnwithpierre.ui.manageData.isValid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val deckRepository: DeckRepository) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())

    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            deckRepository.getAllDeckFromAUser(1).collect { decks ->
                val decksWithSize = decks.map { deck ->
                    // Fetch the size for each deck
                    val size = deckRepository.getSizeOfADeck(deck.deckId)
                    DeckWithSize(deck, size)
                }
                // Update your UI state with the list of decks with their sizes
                _homeUiState.value = HomeUiState(decksWithSize, decksWithSize)
            }
        }
    }

    fun updateFilteredDeck(deckName: String) {
        val filteredList = _homeUiState.value.deckList.filter { it.deck.name.contains(deckName, ignoreCase = true) }
        _homeUiState.value = _homeUiState.value.copy(filteredDeckList = filteredList)
    }
    fun addNewDeck(deck : Deck){
        viewModelScope.launch(Dispatchers.IO) {
            deckRepository.insertDeck(deck)
        }
    }


}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val deckList: List<DeckWithSize> = listOf(), var filteredDeckList: List<DeckWithSize> = listOf())

data class DeckWithSize(
    val deck: Deck, // Assuming Deck is your data class for a deck
    val size: Long
)