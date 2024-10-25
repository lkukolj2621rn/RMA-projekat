package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.search

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData

data class BreedSearchState(
    val items: List<BreedData> = emptyList(),
    val query: String = "",
    val searching: Boolean = false,
    val error: String? = null
)