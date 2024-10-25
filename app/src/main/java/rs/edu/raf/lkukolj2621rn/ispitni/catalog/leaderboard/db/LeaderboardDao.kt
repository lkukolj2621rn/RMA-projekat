package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LeaderboardDao {
    @Insert
    fun insertResult(result: Result)

    @Query("SELECT * FROM result ORDER BY date ASC")
    fun getResults(): List<Result>
}