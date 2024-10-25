package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedRepository
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.ImageData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val repository: BreedRepository,
    val imageLoader: ImageLoader
): ViewModel()  {
    private val _state = MutableStateFlow(BreedDetailsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) =
        _state.getAndUpdate(reducer)
    private lateinit var id: String

    fun setDataId(dataId: String) {
        id = dataId
        setState { copy(id=dataId) }
        viewModelScope.launch {
            var breed: BreedData? = null
            try {
                withContext(Dispatchers.IO) {
                    breed = repository.getBreed(id)
                }
            } catch (e: Exception) {
                setState { copy(error = e.message) }
            } finally {
                setState { copy(data = breed) }
            }

            var image: ImageData? = null
            try {
                image = repository.getRandomImageByBreedId(id)
            } catch (e: Exception) {
                setState { copy(imageError = e.message) }
            } finally {
                setState { copy(imageData = image) }
            }
        }
    }
}