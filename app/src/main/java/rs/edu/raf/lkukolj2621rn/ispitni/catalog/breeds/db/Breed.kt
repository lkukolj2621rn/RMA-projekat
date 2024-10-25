package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Breed(
    @PrimaryKey val id: String,
    val name: String,
    val altNames: String? = null,
    val description: String,
    val origin: String,
    val temperament: String,
    val lifeSpan: String,
    val weight: String,
    val rare: Int,
    val wikipediaUrl: String? = null,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val gotAllImages: Boolean = false,
)