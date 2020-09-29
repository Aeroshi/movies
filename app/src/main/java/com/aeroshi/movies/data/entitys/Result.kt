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



) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result

        if (display_title != other.display_title) return false
        if (mpaa_rating != other.mpaa_rating) return false
        if (critics_pick != other.critics_pick) return false
        if (byline != other.byline) return false
        if (headline != other.headline) return false
        if (summary_short != other.summary_short) return false
        if (publication_date != other.publication_date) return false
        if (opening_date != other.opening_date) return false
        if (date_updated != other.date_updated) return false
        if (link != other.link) return false
        if (multimedia != other.multimedia) return false

        return true
    }

    override fun hashCode(): Int {
        var result = display_title?.hashCode() ?: 0
        result = 31 * result + (mpaa_rating?.hashCode() ?: 0)
        result = 31 * result + (critics_pick ?: 0)
        result = 31 * result + (byline?.hashCode() ?: 0)
        result = 31 * result + (headline?.hashCode() ?: 0)
        result = 31 * result + (summary_short?.hashCode() ?: 0)
        result = 31 * result + (publication_date?.hashCode() ?: 0)
        result = 31 * result + (opening_date?.hashCode() ?: 0)
        result = 31 * result + (date_updated?.hashCode() ?: 0)
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + (multimedia?.hashCode() ?: 0)
        return result
    }
}



