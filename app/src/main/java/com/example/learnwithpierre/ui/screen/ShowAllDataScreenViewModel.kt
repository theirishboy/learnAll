package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.dao.DataRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
class ShowAllDataScreenViewModel(private val dataRepository: DataRepository) : ViewModel() {
    var showAllDataUiState by mutableStateOf(ShowAllDataUiState())
        private set

    var currentCategory by mutableStateOf("Select Category")
    init {
        viewModelScope.launch {
            showAllDataUiState.dataList  = dataRepository.getAllDataStream().map { (it) }.filterNotNull().first()
            showAllDataUiState.dataCategories = dataRepository.getCategories().map { it }.filterNotNull().first()
            showAllDataUiState.filterDataList = showAllDataUiState.dataList
        }
    }
     fun onCategoryChange(newCategory: String){
        currentCategory = newCategory
         showAllDataUiState = showAllDataUiState.copy(
             dataList = showAllDataUiState.dataList,
             dataCategories = showAllDataUiState.dataCategories,
             filterDataList = showAllDataUiState.dataList.filter { it.category == currentCategory }
         )
     }
}
data class ShowAllDataUiState(var dataList: MutableList<Data> = arrayListOf(), var dataCategories: List<String> = listOf(), var filterDataList: List<Data> = listOf())



