package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.dao.FlashCardRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
class ShowAllCardsScreenViewModel(private val flashCardRepository: FlashCardRepository) : ViewModel() {
    var showAllCardsUiState by mutableStateOf(ShowAllCardsUiState())
        private set

    var currentCategory by mutableStateOf("Select Category")
    init {
        viewModelScope.launch {
            showAllCardsUiState.flashCardList  = flashCardRepository.getAllCardStream().map { (it) }.filterNotNull().first()
            showAllCardsUiState.cardCategories = flashCardRepository.getCategories().map { it }.filterNotNull().first()
            showAllCardsUiState.filterFlashCardList = showAllCardsUiState.flashCardList
        }
    }
     fun onCategoryChange(newCategory: String){
        currentCategory = newCategory
         showAllCardsUiState = showAllCardsUiState.copy(
             flashCardList = showAllCardsUiState.flashCardList,
             cardCategories = showAllCardsUiState.cardCategories,
             filterFlashCardList = showAllCardsUiState.flashCardList.filter { it.category == currentCategory }
         )
     }
}
data class ShowAllCardsUiState(var flashCardList: MutableList<FlashCard> = arrayListOf(), var cardCategories: List<String> = listOf(), var filterFlashCardList: List<FlashCard> = listOf())



