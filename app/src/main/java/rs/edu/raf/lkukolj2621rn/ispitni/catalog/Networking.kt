package rs.edu.raf.lkukolj2621rn.ispitni.catalog

import android.content.Context
import coil.ImageLoader
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

val AppJson = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

@Singleton
class Networking @Inject constructor(
    @ApplicationContext context: Context
) {
    val retrofitCat: Retrofit
    val retrofitLeaderboard: Retrofit

    init {
        val okHttpClient = OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "http_cache"), 50*1024*1024))
            .addInterceptor {
                val updatedRequest = it.request().newBuilder()
                    .addHeader(
                        "x-api-key",
                        // INSERT APi KEY
                    )
                    .build()
                it.proceed(updatedRequest)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()


        retrofitCat = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()

        retrofitLeaderboard = Retrofit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader.Builder(context).build()
}
