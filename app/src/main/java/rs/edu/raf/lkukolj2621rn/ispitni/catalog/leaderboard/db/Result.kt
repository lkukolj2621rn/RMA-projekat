package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant

@Entity(indices = [Index(value = ["date"], orders = [Index.Order.ASC])])
@TypeConverters(Result.InstantConverter::class)
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val result: Float,
    val date: Instant
) {
    class InstantConverter {
        @TypeConverter
        fun toLong(i: Instant): Long = i.epochSecond

        @TypeConverter
        fun toInstant(l: Long): Instant = Instant.ofEpochSecond(l)
    }
}