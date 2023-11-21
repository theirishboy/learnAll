package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Card
import com.example.learnwithpierre.dao.CardRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
class ShowAllCardsScreenViewModel(private val cardRepository: CardRepository) : ViewModel() {
    var showAllCardsUiState by mutableStateOf(ShowAllCardsUiState())
        private set

    var currentCategory by mutableStateOf("Select Category")
    init {
        viewModelScope.launch {
            showAllCardsUiState.cardList  = cardRepository.getAllCardStream().map { (it) }.filterNotNull().first()
            showAllCardsUiState.cardCategories = cardRepository.getCategories().map { it }.filterNotNull().first()
            showAllCardsUiState.filterCardList = showAllCardsUiState.cardList
        }
    }
     fun onCategoryChange(newCategory: String){
        currentCategory = newCategory
         showAllCardsUiState = showAllCardsUiState.copy(
             cardList = showAllCardsUiState.cardList,
             cardCategories = showAllCardsUiState.cardCategories,
             filterCardList = showAllCardsUiState.cardList.filter { it.category == currentCategory }
         )
     }
}
data class ShowAllCardsUiState(var cardList: MutableList<Card> = arrayListOf(), var cardCategories: List<String> = listOf(), var filterCardList: List<Card> = listOf())



