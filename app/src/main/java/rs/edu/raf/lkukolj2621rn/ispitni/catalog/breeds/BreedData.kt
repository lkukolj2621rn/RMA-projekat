package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds

data class BreedData(
    val id: String,
    val name: String,
    val alternateNames: List<String>,
    val description: String,
    val origins: List<String>,
    val temperament: List<String>,
    val lifespan: String,
    val weight: String,
    val rare: Boolean,
    val wikipediaLink: String?,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    var gotAllImages: Boolean = false,
)
