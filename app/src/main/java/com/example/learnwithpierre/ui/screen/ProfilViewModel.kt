package com.example.learnwithpierre.ui.screen

import androidx.lifecycle.ViewModel
import com.example.learnwithpierre.dao.Deck
import com.example.learnwithpierre.dao.FlashCard
import com.example.learnwithpierre.dao.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfilViewModel(userRepository: UserRepository) : ViewModel() {
    private val _profilUiState = MutableStateFlow(ProfilUiState.Success("jean","jean"))
    val profilUiState: StateFlow<ProfilUiState> = _profilUiState.asStateFlow()

    init {

    }

}

sealed class ProfilUiState {
    data class Success(val name: String, val email : String): ProfilUiState()
    data class Error(val exception: Throwable): ProfilUiState()
}
