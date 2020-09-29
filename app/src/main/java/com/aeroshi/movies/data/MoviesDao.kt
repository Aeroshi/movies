package com.aeroshi.movies.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aeroshi.movies.data.entitys.Result


@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getMovies(): List<Result>

    @Insert
    fun insertData(configManager: List<Result>)

    @Query("DELETE FROM movies")
    fun deleteAll()
}




