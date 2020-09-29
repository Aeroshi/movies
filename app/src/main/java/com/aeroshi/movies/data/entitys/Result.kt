package com.aeroshi.movies.data.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Result(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val display_title: String?,
    val mpaa_rating: String?,
    val critics_pick: Int?,
    val byline: String?,
    val headline: String?,
    val summary_short: String?,
    val publication_date: String?,
    val opening_date: String?,
    val date_updated: String?,
    val link: Link?,
    val multimedia: Multimedia?
)



