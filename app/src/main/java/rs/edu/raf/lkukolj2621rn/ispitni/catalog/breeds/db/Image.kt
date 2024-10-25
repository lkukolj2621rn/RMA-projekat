package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey val id: String,
    val url: String,
    val breedId: String
)
