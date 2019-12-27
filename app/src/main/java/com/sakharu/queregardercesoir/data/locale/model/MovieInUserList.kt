package com.sakharu.queregardercesoir.data.locale.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movieInUserList",indices = [Index(value = ["idUserList","idMovie"], unique = true)])
data class MovieInUserList(

    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    var id:Long?,

    @ForeignKey(entity = Category::class,parentColumns = ["idUserList"],childColumns = ["id"], onDelete = ForeignKey.CASCADE)
    @field:SerializedName("idUserList")
    var idUserList:Long,

    @ForeignKey(entity = Movie::class,parentColumns = ["idMovie"],childColumns = ["id"], onDelete = ForeignKey.CASCADE)
    @field:SerializedName("idMovie")
    var idMovie:Long
)