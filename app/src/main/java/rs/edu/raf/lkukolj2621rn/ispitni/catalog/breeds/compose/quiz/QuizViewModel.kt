package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedRepository
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.LeaderboardRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: BreedRepository,
    private val leaderboardRepository: LeaderboardRepository,
    val imageLoader: ImageLoader
) : ViewModel() {
    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()
    private fun setState(reducer: QuizState.() -> QuizState) =
        _state.getAndUpdate(reducer)

    private val timer = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            while (state.value.timeLeft > 0) {
                delay(1.seconds)
                setState { copy(timeLeft = timeLeft - 1) }
            }
        }
        if (state.value.result == null)
            calculateResult()
    }
    
    private val breeds = repository.observeBreeds()
    private val seenImages = hashSetOf<String>()
    
    init {
        viewModelScope.launch {
            repository.loadBreeds()
            generateQuestion()
        }
    }
    
    private fun getRandomBreed(seen: Set<BreedData>): BreedData {
        var b = breeds.value.random()
        while (seen.contains(b))
            b = breeds.value.random()
        return b
    }

    private suspend fun generateQuestion() {
        var question = ""
        var answers = listOf<String>()
        var correctAnswer = ""
        var b: BreedData
        withContext(Dispatchers.Default) {
            while (true) {
                b = breeds.value.random()
                when(listOf(1, 2, 3).random()) {
                    1 -> {
                        question = "Guess the breed!"
                        answers = listOf(b.name)
                        correctAnswer = answers[0]
                        val seen = hashSetOf(b)
                        var i = 3
                        while (i > 0) {
                            val b = getRandomBreed(seen)
                            answers += b.name
                            seen.add(b)
                            i--
                        }
                    }
                    2 -> {
                        question = "Guess the odd one out!"
                        answers = b.temperament.shuffled().take(3)
                        correctAnswer = answers[0]
                        if (answers.count() < 3)
                            continue
                        while (true) {
                            val b2 = getRandomBreed(setOf(b))
                            val l = b2.temperament.filterNot(b.temperament::contains)
                            if (l.isEmpty())
                                continue
                            answers += l.random()
                            break
                        }
                    }
                    3 -> {
                        question = "Guess the temperament!"
                        answers = listOf(b.temperament.random())
                        correctAnswer = answers[0]
                        while (answers.count() < 4) {
                            val b2 = getRandomBreed(setOf(b))
                            val l = b2.temperament.filterNot(b.temperament::contains)
                            answers += l.shuffled().take(4-answers.count())
                        }
                    }
                }
                answers = answers.shuffled()
                break
            }
        }
        setState { copy(answers = answers, correctAnswer = correctAnswer, question = question, loading = false, image = null, imageError = null) }
        withContext(Dispatchers.IO) {
            try {
                val i = repository.getRandomImageByBreedId(b.id)
                setState { copy(image = i) }
            } catch (e: Exception) {
                setState { copy(imageError = e.message) }
            }
        }
    }

    fun observeEvent(event: QuizUiEvent) {
        when (event) {
            is QuizUiEvent.Answer -> {
                if (setState {copy(loading = true)}.loading)
                    return
                if (state.value.correctAnswer == event.answer)
                    setState { copy(correct=correct+1) }
                if (state.value.questionNumber == 20) {
                    timer.cancel()
                    viewModelScope.launch {calculateResult()}
                }
                else {
                    setState { copy(questionNumber = questionNumber+1) }
                    viewModelScope.launch { generateQuestion() }
                }
            }
            QuizUiEvent.Back -> setState { copy(exitDialogShown=true) }
            QuizUiEvent.ConfirmBack -> setState { copy(leaving=true) }
            QuizUiEvent.DismissDialog -> setState { copy(exitDialogShown=false) }
            QuizUiEvent.NoSend -> setState { copy(leaving=true) }
            QuizUiEvent.YesSend -> {
                if (setState { copy(sending=true) }.sending)
                    return
                viewModelScope.launch {
                    try {
                        withContext(Dispatchers.IO) {
                            leaderboardRepository.sendResult(state.value.result!!)
                        }
                        setState { copy(leaving = true) }
                    }
                    catch (e: Exception) {
                        setState { copy(sendError = e.message) }
                    }
                }
            }
        }
    }

    private suspend fun calculateResult() {
        val ubp = state.value.correct * 2.5f * (1 + (state.value.timeLeft + 120) / 300f)
        val r = ubp.coerceAtMost(maximumValue = 100f)
        leaderboardRepository.recordResult(r)
        setState { copy(result = r) }
    }
}