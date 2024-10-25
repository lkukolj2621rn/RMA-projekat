package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db

import androidx.room.Embedded
import androidx.room.Relation

data class BreedWithImages(
    @Embedded val breed: Breed,
    @Relation(
        parentColumn = "id",
        entityColumn = "breedId"
    ) val images: List<Image>
)