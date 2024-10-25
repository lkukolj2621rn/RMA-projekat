package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api

import kotlinx.serialization.Serializable

@Serializable
data class ResultApiData(
    val nickname: String,
    val result: Float,
    val category: Int = 1,
    val createdAt: Long
)
