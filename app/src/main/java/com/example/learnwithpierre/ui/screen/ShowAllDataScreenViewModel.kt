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
class ShowAllDataScreenViewModel(private val cardRepository: CardRepository) : ViewModel() {
    var showAllDataUiState by mutableStateOf(ShowAllDataUiState())
        private set

    var currentCategory by mutableStateOf("Select Category")
    init {
        viewModelScope.launch {
            showAllDataUiState.cardList  = cardRepository.getAllCardStream().map { (it) }.filterNotNull().first()
            showAllDataUiState.dataCategories = cardRepository.getCategories().map { it }.filterNotNull().first()
            showAllDataUiState.filterCardList = showAllDataUiState.cardList
        }
    }
     fun onCategoryChange(newCategory: String){
        currentCategory = newCategory
         showAllDataUiState = showAllDataUiState.copy(
             cardList = showAllDataUiState.cardList,
             dataCategories = showAllDataUiState.dataCategories,
             filterCardList = showAllDataUiState.cardList.filter { it.category == currentCategory }
         )
     }
}
data class ShowAllDataUiState(var cardList: MutableList<Card> = arrayListOf(), var dataCategories: List<String> = listOf(), var filterCardList: List<Card> = listOf())



