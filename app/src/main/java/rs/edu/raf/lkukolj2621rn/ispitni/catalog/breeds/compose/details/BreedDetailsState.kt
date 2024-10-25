package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.details

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.ImageData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData

data class BreedDetailsState(
    val id: String? = null,
    val data: BreedData? = null,
    val error: String? = null,
    val imageData: ImageData? = null,
    val imageError: String? = null
)
