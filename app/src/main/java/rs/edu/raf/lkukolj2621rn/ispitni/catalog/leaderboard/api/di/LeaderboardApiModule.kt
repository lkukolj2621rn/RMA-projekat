package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.Networking
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.api.LeaderboardApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardApiModule {

    @Provides
    @Singleton
    fun provideLeaderboardApi(networking: Networking): LeaderboardApi = networking.retrofitLeaderboard.create()
}
