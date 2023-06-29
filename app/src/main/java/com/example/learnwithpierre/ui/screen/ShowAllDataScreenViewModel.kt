package com.example.learnwithpierre.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.dao.Data
import com.example.learnwithpierre.dao.DataRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ShowAllDataScreenViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private var data1 by mutableStateOf(Data(3,"2GM","1945",true,"Histoire",1, LocalDateTime.now()))
    private var data2 by mutableStateOf(Data(4052,"1GM","1918",true,"Histoire",1, LocalDateTime.now()))
    private var data3 by mutableStateOf(Data(1563,"start 2GM","1939",true,"Histoire",1, LocalDateTime.now()))
    var table : List<Data> = arrayListOf(data1,data2,data3)
    init {
        viewModelScope.launch {}
    }
}