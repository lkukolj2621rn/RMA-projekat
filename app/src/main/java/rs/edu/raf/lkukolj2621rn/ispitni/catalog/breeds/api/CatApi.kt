package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApi {
    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiData>

    @GET("breeds/{id}")
    suspend fun getBreed(
        @Path("id") id: String,
    ): BreedApiData

    @GET("images/search")
    suspend fun getImages(
        @Query("breed_ids") breedIds: String,
        @Query("limit") limit: Int,
        @Query("order") order: String,
        @Query("page") page: Int?,
    ): Response<List<ImageApiData>>
}