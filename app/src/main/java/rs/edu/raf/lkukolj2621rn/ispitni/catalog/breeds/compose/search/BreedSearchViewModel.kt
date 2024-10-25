package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.search

import android.util.Log
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
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreedSearchViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BreedSearchState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedSearchState.() -> BreedSearchState) =
        _state.getAndUpdate(reducer)

    init {
        fetchBreeds()
        setSearch("")
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

    fun setSearch(q: String) {
        setState { copy(query = q, searching = true) }
        viewModelScope.launch {
            val items: List<BreedData>
            withContext(Dispatchers.Default) {
                items = repository.searchBreeds(q)
            }
            Log.d("search", items.toString())
            if (state.value.query == q)
                setState { copy(items = items, searching = false) }
        }
    }
}