package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.details

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.LocalResultData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.ProfileData

data class DetailsState(
    val profileData: ProfileData? = null,
    val navigateEdit: Boolean = false,
    val results: List<LocalResultData> = listOf(),
    val resultsLoading: Boolean = true,
    val resultsError: String? = null,
    val bestResult: Float? = null,
    val leaderboardBest: Int? = null,
    val leaderboardLoading: Boolean = true,
    val leaderboardError: String? = null,
)
