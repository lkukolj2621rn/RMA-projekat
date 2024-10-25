package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api

import kotlinx.serialization.Serializable

@Serializable
data class SubmittedResultData(
    val result: ResultApiData,
    val ranking: Int
)
