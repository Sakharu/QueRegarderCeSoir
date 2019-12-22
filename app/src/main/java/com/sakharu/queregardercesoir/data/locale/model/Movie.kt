package com.sakharu.queregardercesoir.data.locale.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie (

    @PrimaryKey
    @field:SerializedName("id")
    var id:Long,

    @field:SerializedName("title")
    var title:String,

    @field:SerializedName("genre_ids")
    var genresId : List<Long>,

    @field:SerializedName("overview")
    var overview:String="",

    @field:SerializedName("popularity")
    var popularity:Double,

    @field:SerializedName("poster_path")
    var posterImg:String?="",

    @field:SerializedName("backdrop_path")
    var backdropImg:String?="",

    @field:SerializedName("release_date")
    var releaseDate:String?,

    @field:SerializedName("original_title")
    var original_title:String?,

    @field:SerializedName("vote_average")
    var vote_average:Double?,

    @field:SerializedName("budget")
    var budget:Int?,

    @field:SerializedName("vote_count")
    var vote_count:Int?,

    @field:SerializedName("releaseYear")
    var releaseYear:Int?
)
{
    override fun equals(other: Any?): Boolean{
        return this.id == (other as Movie).id
    }

}
