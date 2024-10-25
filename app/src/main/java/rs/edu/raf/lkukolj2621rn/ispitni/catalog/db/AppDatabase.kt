package rs.edu.raf.lkukolj2621rn.ispitni.catalog.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.Breed
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.BreedsDao
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.Image
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.db.LeaderboardDao
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.db.Result


@Database(
    entities = [
        Breed::class,
        Image::class,
        Result::class
    ],
    version = 5,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedsDao(): BreedsDao
    abstract fun leaderboardDao(): LeaderboardDao

}
