package com.aeroshi.movies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aeroshi.movies.data.entitys.Link
import com.aeroshi.movies.data.entitys.Multimedia
import com.aeroshi.movies.data.entitys.Result
import com.aeroshi.movies.util.Constants.Companion.DATABASE_NAME


@Database(
    entities = [Result::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MoviesDao(): MoviesDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }

}
