package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api

import kotlinx.serialization.Serializable

@Serializable
data class ImageApiData(
    val id: String,
    val url: String,
)
