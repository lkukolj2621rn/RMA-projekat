package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api

import kotlinx.serialization.Serializable

@Serializable
data class BreedApiData(
    val id: String,
    val name: String,
    val alt_names: String? = null,
    val description: String,
    val origin: String,
    val temperament: String,
    val life_span: String,
    val weight: WeightData,
    val rare: Int,
    val wikipedia_url: String? = null,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
) {
    @Serializable
    data class WeightData(
        val metric: String
    )
}
