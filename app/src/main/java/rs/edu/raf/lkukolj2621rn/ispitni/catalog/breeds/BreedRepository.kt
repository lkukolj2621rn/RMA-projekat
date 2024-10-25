package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import retrofit2.Response
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api.BreedApiData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api.CatApi
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api.ImageApiData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.Breed
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.Image
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.db.AppDatabase
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinDuration

@Singleton
class BreedRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val api: CatApi
) {
    private val breeds = MutableStateFlow(listOf<BreedData>())

    suspend fun loadBreeds() {
        if (breeds.value.isEmpty())
            breeds.update {
                var breeds = appDatabase.breedsDao().getBreeds()
                if (breeds.isEmpty()) {
                    withContext(Dispatchers.IO) {
                        breeds = api.getAllBreeds().map(BreedApiData::asBreed)
                    }
                    appDatabase.breedsDao().upsertBreeds(breeds)
                }
                breeds.map(Breed::asBreedData)
            }
    }

    suspend fun getBreed(id: String): BreedData {
        val breed = withContext(Dispatchers.Default) {
            breeds.value.find { it.id == id }
        }
        if (breed != null)
            return breed
        var b = appDatabase.breedsDao().getBreed(id)
        if (b == null)
            b = withContext(Dispatchers.IO) {
                val b = api.getBreed(id).asBreed()
                appDatabase.breedsDao().upsertBreeds(listOf(b))
                b
            }
        return b.asBreedData()
    }

    fun observeBreeds() = breeds.asStateFlow()

    fun searchBreeds(query: String) =
        breeds.value.filter { it.name.contains(query, true) }

    suspend fun getRandomImageByBreedId(breedId: String): ImageData {
        val candidates = appDatabase.breedsDao().getImageIdsByBreedId(breedId)
        return withContext(Dispatchers.Default) {
            if (appDatabase.breedsDao().getBreed(breedId)?.gotAllImages != true)
                withContext(Dispatchers.IO) {
                    val response = api.getImages(breedId, 1, "RANDOM", null)
                    val image = response.body()!![0].asImage(breedId)
                    appDatabase.breedsDao().upsertImages(listOf(image))
                    image.asImageData()
                }
            else appDatabase.breedsDao().getImage(candidates.random()).asImageData()
        }
    }

    suspend fun getAllImagesByBreedId(breedId: String, scope: CoroutineScope): Flow<ImageData?> {
        val b = getBreed(breedId)
        if (b.gotAllImages)
            return appDatabase.breedsDao().getImagesByBreedId(breedId).asFlow<Image?>()
                .onCompletion { emit(null) }.map { it?.asImageData() }
        else {
            val list: MutableList<Image> = mutableListOf()
            return flow<ImageData?> {
                var i = 0
                var response: Response<List<ImageApiData>>? = null
                do {
                    if (response != null && response.code() == 429)
                        delay((response.headers()["retry-after"]!!.toInt() + 1).seconds)
                    response = api.getImages(breedId, 100, "ASC", i++)
                    val converted = response.body()!!.map { it.asImage(breedId) }
                    list.addAll(converted)
                    converted.forEach { emit(it.asImageData()) }
                } while (response!!.headers()["pagination-count"]!!.toInt() > (i + 1) * response.headers()["pagination-limit"]!!.toInt())
            }
                .onCompletion {
                    if (it == null) {
                        appDatabase.withTransaction {
                            appDatabase.breedsDao().upsertImages(list)
                            appDatabase.breedsDao().updateBreed(
                                appDatabase.breedsDao().getBreed(breedId)!!
                                    .copy(gotAllImages = true)
                            )
                        }
                        b.gotAllImages = true
                        emit(null)
                    }
                }
                .shareIn(scope, SharingStarted.Lazily)
        }
    }
}