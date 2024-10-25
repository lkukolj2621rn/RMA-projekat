package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api

import kotlinx.serialization.Serializable

@Serializable
data class SubmitResultData(
    val nickname: String,
    val result: Float,
    val category: Int
)