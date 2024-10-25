package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BreedListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedListState.() -> BreedListState) =
        _state.getAndUpdate(reducer)

    init {
        observeBreeds()
        fetchBreeds()
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            repository.observeBreeds().collect {
                setState { copy(list = it) }
            }
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.loadBreeds()
                }
            } catch (error: IOException) {
                setState { copy(error = error.message)}
            }
        }
    }
}