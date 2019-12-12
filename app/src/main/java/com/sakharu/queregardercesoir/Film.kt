package com.sakharu.queregardercesoir

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.Genre
import com.google.gson.annotations.SerializedName

@Entity(tableName = "film")
data class Film (
    @PrimaryKey
    @field:SerializedName("id")
    var id:Int,
    @field:SerializedName("title")
    var title:String,
    //@field:SerializedName("genre_ids")
    //var genres : ArrayList<Int>,
    @field:SerializedName("overview")
    var overview:String,
    @field:SerializedName("popularity")
    var popularity:Int,
    @field:SerializedName("poster_path")
    var posterImg:String,
    @field:SerializedName("backdrop_path")
    var backdropImg:String,
    @field:SerializedName("release_date")
    var releaseDate:String
)