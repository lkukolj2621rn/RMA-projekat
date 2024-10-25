package rs.edu.raf.lkukolj2621rn.ispitni.catalog.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.db.AppDatabase
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.db.AppDatabaseBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        builder: AppDatabaseBuilder
    ): AppDatabase {
        return builder.build()
    }

//    @Singleton
//    @Provides
//    fun provideDatabase(
//        @ApplicationContext context: Context,
//    ): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java,
//            "rma.db",
//        )
////            .fallbackToDestructiveMigration()
////            .addMigrations(MIGRATION_2_3)
//            .build()
//    }



}
