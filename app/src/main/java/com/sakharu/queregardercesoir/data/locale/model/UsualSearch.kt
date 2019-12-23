package com.sakharu.queregardercesoir.data.locale.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usualSearch")
data class UsualSearch(

    @PrimaryKey
    @field:SerializedName("id")
    var id:Int,

    @field:SerializedName("title")
    var title:String,

    @field:SerializedName("subtitle")
    var subtitle:String
)