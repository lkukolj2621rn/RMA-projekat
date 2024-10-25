package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.compose

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedRepository
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz.QuizState
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.LeaderboardRepository
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.ResultData
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()
    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) =
        _state.getAndUpdate(reducer)

    init {
        viewModelScope.launch {
            var l: List<ResultData>? = null
            var error: String? = null
            withContext(Dispatchers.IO) {
                try {
                    val list = repository.getResults()
                    withContext(Dispatchers.Default) {
                        val m = hashMapOf<String, Int>()
                        list.forEach {
                            m[it.nickname] = m.getOrDefault(it.nickname, 0) + 1
                        }
                        l = list.sortedByDescending { it.result }.mapIndexed { index, resultData ->
                            resultData.copy(quizNumber = m[resultData.nickname]!!, ranking = index + 1) }
                    }
                }
                catch (e: Exception) {
                    error = e.message
                }
            }
            setState { copy(leaderboard=l, error=error) }
        }
    }
}