package rs.edu.raf.rma.photos.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.Networking
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api.CatApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatApiModule {

    @Provides
    @Singleton
    fun provideCatApi(networking: Networking): CatApi = networking.retrofitCat.create()
}
