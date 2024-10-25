package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.LeaderboardRepository
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.LocalResultData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.ProfileDataStore
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val profileDataStore: ProfileDataStore,
    private val repository: LeaderboardRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: DetailsState.() -> DetailsState) =
        _state.getAndUpdate(reducer)

    init {
        observeProfile()
        viewModelScope.launch {
            var list: List<LocalResultData> = listOf()
            var best: Float? = 0f
            var error: String? = null
            withContext(Dispatchers.Default) {
                try {
                    list = repository.getLocalResults()
                    best = list.maxOfOrNull { it.result }
                } catch (e: Exception) {
                    error = e.message
                }
            }
            setState { copy(resultsLoading = false, resultsError = error, results = list, bestResult = best) }
        }
        viewModelScope.launch {
            var ranking: Int? = null
            var error: String? = null
            val nickname = profileDataStore.data.value.nickname
            withContext(Dispatchers.IO) {
                try {
                    val list = repository.getResults()
                    withContext(Dispatchers.Default) {
                        ranking = list.sortedByDescending { it.result }.mapIndexed { index, resultData ->
                            if(resultData.nickname == nickname)
                                index + 1
                            else
                                null
                        }.firstNotNullOfOrNull { it }
                    }
                } catch (e: Exception) {
                    error = e.message
                }
            }
            setState { copy(leaderboardLoading = false, leaderboardError = error, leaderboardBest = ranking) }
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            profileDataStore.data.collect {
                setState { copy(profileData = it) }
            }
        }
    }
}