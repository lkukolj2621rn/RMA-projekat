package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.gallery

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedRepository
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.ImageData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.Image
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: BreedRepository,
    val imageLoader: ImageLoader,
    @ApplicationContext private val context: Context
): ViewModel()  {
    private val _state = MutableStateFlow(GalleryState())
    val state = _state.asStateFlow()
    private fun setState(reducer: GalleryState.() -> GalleryState) =
        _state.getAndUpdate(reducer)
    private lateinit var id: String
    private var list: MutableList<ImageData> = mutableListOf()

    fun setDataId(dataId: String) {
        id = dataId
        setState { copy(id=dataId) }
        observeImages()
    }

    private fun observeImages() {
        viewModelScope.launch {
            repository.getAllImagesByBreedId(id, viewModelScope)
                .catch {
                    setState { copy(error = it.message) }
                    emit(null)
                }
                .collect {
                    if (it == null)
                        setState { copy(loading = false) }
                    else {
                        list.add(it)
                        val l = list
                        setState { copy(list = l.toList()) }
                }
            }
        }
    }
}