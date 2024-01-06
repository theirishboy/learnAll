package com.example.learnwithpierre.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Deck
import com.example.learnwithpierre.dao.DeckRepository
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.dao.User
import com.example.learnwithpierre.dao.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val deckRepository: DeckRepository, private val userRepository: UserRepository) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())

    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {


        viewModelScope.launch(Dispatchers.IO) {
            val newUser = User(0, "Test", "Test", LocalDateTime.now(), LocalDateTime.now())
            val generatedUserId = userRepository.insertUser(newUser)
            deckRepository.getAllDeckFromAUser(1).collect { decks ->
                val decksWithSize = decks.map { deck ->

                    // Fetch the size for each deck
                    val size = async {
                        deckRepository.getSizeOfADeck(deck.deckId)
                    }
                    val loadSize = size.await()
                    DeckWithSize(deck, loadSize)

                }
                _homeUiState.value = HomeUiState(decksWithSize, decksWithSize)
                println("viewModel is called")
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