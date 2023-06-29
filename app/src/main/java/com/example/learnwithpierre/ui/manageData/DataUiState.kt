package com.example.learnwithpierre.ui.manageData

import com.example.learnwithpierre.dao.Data
import java.time.LocalDateTime
import java.util.Date

data class DataUiState(
    val id: Int = 0,
    val recto: String = "",
    val verso: String = "",
    val category: String = "",
    val isRecto: Boolean = false,
    val score: Int = 0,
    val date: LocalDateTime = LocalDateTime.now(),
    val actionEnabled: Boolean = false

)

/**
 * Extension function to convert [DataUiState] to [Item]. If the value of [DataUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [DataUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun DataUiState.toData(): Data = Data(
    id = id,
    recto = recto,
    verso = verso,
    category = category,
    isRecto = isRecto,
    score = score,
    dateModification = date,

)

/**
 * Extension function to convert [Item] to [DataUiState]
 */
//add a function the data type to allow every member of type data to be change to DataUiState.
fun Data.toDataUiState(actionEnabled: Boolean = false): DataUiState = DataUiState(
    id = id,
    recto = recto,
    verso = verso,
    category = category,
    isRecto = isRecto,
    score = score,
    actionEnabled = actionEnabled

)

//Check that nothing is missing
fun DataUiState.isValid() : Boolean {
    return recto.isNotBlank() && verso.isNotBlank() && category.isNotBlank()
}
