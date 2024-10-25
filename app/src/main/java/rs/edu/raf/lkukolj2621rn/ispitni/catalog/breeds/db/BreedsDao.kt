package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsDao {
    @Upsert
    suspend fun upsertBreeds(breeds: List<Breed>)

    @Upsert
    suspend fun upsertImages(images: List<Image>)

    @Update
    suspend fun updateBreed(breed: Breed)

    @Query("SELECT * FROM image WHERE breedId = :breedId")
    suspend fun getImagesByBreedId(breedId: String): List<Image>

    @Query("SELECT * FROM image WHERE id = :id")
    suspend fun getImage(id: String): Image

    @Query("SELECT id FROM image WHERE breedId = :breedId")
    suspend fun getImageIdsByBreedId(breedId: String): List<String>

    @Query("SELECT * FROM breed")
    suspend fun getBreeds(): List<Breed>

    @Query("SELECT * FROM breed WHERE id = :id")
    suspend fun getBreed(id: String): Breed?
}