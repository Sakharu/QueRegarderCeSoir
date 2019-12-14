package com.sakharu.queregardercesoir.data.locale.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*
    Genre représente les différents genre disponibles pour un film comme par exemple,
    action, aventure, science-fiction...
 */
@Entity(tableName = "genre")
data class Genre(

    @PrimaryKey
    @field:SerializedName("id")
    var id:Long,

    @field:SerializedName("name")
    var name:String="unknown"
)