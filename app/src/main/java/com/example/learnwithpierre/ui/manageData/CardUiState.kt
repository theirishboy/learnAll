package com.example.learnwithpierre.ui.manageData

import com.example.learnwithpierre.dao.FlashCard
import java.time.LocalDateTime

data class CardUiState(
    val cardId: Long = 0,
    val deckId: Long = 1,
    val recto: String = "",
    val verso: String = "",
    val category: String = "",
    val isRecto: Boolean = false,
    val score: Int = 0,
    val date: LocalDateTime = LocalDateTime.now(),
    val dateTraining: LocalDateTime = LocalDateTime.now(),
    val actionEnabled: Boolean = false

)

/**
 * Extension function to convert [CardUiState] to [Item]. If the value of [CardUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [CardUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun CardUiState.toData(): FlashCard = FlashCard(
    cardId = cardId,
    deckId = deckId,
    recto = recto,
    verso = verso,
    category = category,
    isRecto = isRecto,
    score = score,
    dateModification = date,
    dateTraining = dateTraining

)

/**
 * Extension function to convert [Item] to [CardUiState]
 */
//add a function the data type to allow every member of type data to be change to DataUiState.
fun FlashCard.toDataUiState(actionEnabled: Boolean = false): CardUiState = CardUiState(
    cardId = cardId,
    deckId = deckId,
    recto = recto,
    verso = verso,
    category = category,
    isRecto = isRecto,
    score = score,
    actionEnabled = actionEnabled

)

//Check that nothing is missing
fun CardUiState.isValid() : Boolean {
    return recto.isNotBlank() && verso.isNotBlank() && category.isNotBlank()
}
