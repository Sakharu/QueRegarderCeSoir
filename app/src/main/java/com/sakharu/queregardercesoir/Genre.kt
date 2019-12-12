package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "genre")
data class Genre(
    @PrimaryKey
    @field:SerializedName("id")
    var id:Int,
    @field:SerializedName("name")
    var name:String="unknown"
)