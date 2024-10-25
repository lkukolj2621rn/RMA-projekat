package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.db.AppDatabase
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api.LeaderboardApi
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api.ResultApiData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api.SubmitResultData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.db.Result
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.ProfileDataStore
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val profileDataStore: ProfileDataStore,
    private val api: LeaderboardApi
) {
    suspend fun sendResult(result: Float) {
        api.submitResult(SubmitResultData(result = result,
            category = 1,
            nickname = profileDataStore.data.value.nickname))
    }

    suspend fun recordResult(result: Float) {
        withContext(Dispatchers.IO) {
            appDatabase.leaderboardDao().insertResult(Result(0, result, Instant.now()))
        }
    }

    suspend fun getResults(): List<ResultData> =
        api.getLeaderboard(1).filter { it.category == 1 }.map(::asResultData)

    private fun asResultData(resultApiData: ResultApiData): ResultData =
        ResultData(resultApiData.nickname, resultApiData.result)

    suspend fun getLocalResults(): List<LocalResultData> =
        withContext(Dispatchers.IO) {
            appDatabase.leaderboardDao().getResults().map(::toLocalResultData)
        }

    private fun toLocalResultData(r: Result) = LocalResultData(r.result, r.date)
}