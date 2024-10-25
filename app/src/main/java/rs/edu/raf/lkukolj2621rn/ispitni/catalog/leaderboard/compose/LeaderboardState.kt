package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.compose

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.ResultData

data class LeaderboardState(
    val leaderboard: List<ResultData>? = null,
    val error: String? = null
)
