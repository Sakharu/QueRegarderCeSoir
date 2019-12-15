package com.sakharu.queregardercesoir.data.remote

import com.google.gson.annotations.SerializedName
import com.sakharu.queregardercesoir.data.locale.model.Genre

data class ResponseMovieDetail(

    @field:SerializedName("id")
    var id:Long,

    @field:SerializedName("title")
    var title:String,

    @field:SerializedName("genres")
    var genres : ArrayList<Genre>,

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
    var vote_count:Int?
)