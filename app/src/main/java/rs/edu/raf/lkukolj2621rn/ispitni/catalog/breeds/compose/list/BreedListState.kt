package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData

data class BreedListState(
    val list: List<BreedData> = emptyList(),
    val error: String? = null
)