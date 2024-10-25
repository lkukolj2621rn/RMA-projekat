package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(@Query("category") category: Int): List<ResultApiData>

    @POST("leaderboard")
    suspend fun submitResult(@Body data: SubmitResultData): SubmittedResultData
}