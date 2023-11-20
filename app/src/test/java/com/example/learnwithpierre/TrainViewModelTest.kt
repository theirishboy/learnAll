package com.example.learnwithpierre

import com.example.learnwithpierre.ui.screen.TrainViewModel
import com.example.learnwithpierre.dao.Card
import com.example.learnwithpierre.dao.CardRepository
import com.example.learnwithpierre.ui.screen.AnswerState
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before

class TestDataRepository : CardRepository {
    private val testData: List<Card> = listOf(
        Card(1, "demo1", "vide1", false, "animal1", 1, LocalDateTime.now()),
        Card(2, "demo2", "vide2", false, "animal2", 2, LocalDateTime.now())
    )

    override fun getAllDataStream(): Flow<MutableList<Card>> {
        // Implement this method if needed for your tests
        TODO()
    }

    override fun getDataStream(id: Int): Flow<Card?> {
        // Implement this method if needed for your tests
        TODO()
    }

    override suspend fun insertData(card: Card) {
        // Implement this method if needed for your tests
        TODO()
    }

    override suspend fun deleteData(card: Card) {
        // Implement this method if needed for your tests
        TODO()
    }

    override suspend fun updateData(card: Card) {
        // Implement this method if needed for your tests
        TODO()
    }

    override fun getRandomData(): Flow<List<Card>> {
        return flowOf(testData)
    }

    override fun getCategories(): Flow<MutableList<String>> {
        // Implement this method if needed for your tests
        TODO()
    }
}
class TrainViewModelTest {
    private var learnApplication = LearnApplication()

    private var item1 = Card(1, "Apples", "Pear", false,"fruit",1, LocalDateTime.now())
    private var item2 = Card(2, "Bananas", "Peach", false,"fruit",1, LocalDateTime.now())

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun trainViewModel_CorrectScoreCalculation() = runBlocking {
        val dataRepository = TestDataRepository()
        val viewModel = TrainViewModel(dataRepository)
        val trainUiState = viewModel.trainUiState
        trainUiState.cardList = mutableListOf(item1, item2) // Use mutableListOf() to create a mutable list


        viewModel.nextQuestion()

        val trainUiScore = viewModel.trainUiScore
        val showAnswerState = viewModel.showAnswerPopUp
        val size = 2
        val currentQuestion = viewModel.currentQuestion


        assertEquals(AnswerState.NOTSHOW,showAnswerState)
        assertEquals(1f/size,trainUiScore.toFloat())
        assertEquals("",trainUiState.answer)
        assertEquals(trainUiState.cardList.last(), item1)
        assertEquals(currentQuestion, item1)
    }
    @Test
    fun trainViewModel_PopUpState_CompareData_GoodAnswer() = runBlocking {
        val dataRepository = TestDataRepository()
        val viewModel = TrainViewModel(dataRepository)

        val trainUiState = viewModel.trainUiState
        trainUiState.answer = "Good answer"
        trainUiState.cardList.last().verso = "Good answer"
        val showAnswerState = viewModel.showAnswerPopUp
        viewModel.compareData()
        assertEquals(AnswerState.TRUE, viewModel.showAnswerPopUp)
        assertNotEquals(showAnswerState, viewModel.showAnswerPopUp)
    }
    @Test
    fun trainViewModel_PopUpState_CompareData_BadAnswer() = runBlocking {
        val dataRepository = TestDataRepository()
        val viewModel = TrainViewModel(dataRepository)

        val trainUiState = viewModel.trainUiState
        trainUiState.answer = "Bad answer"
        trainUiState.cardList.last().verso = "Badanswer"

        viewModel.compareData()
        assertEquals(AnswerState.FALSE,viewModel.showAnswerPopUp)
    }

}