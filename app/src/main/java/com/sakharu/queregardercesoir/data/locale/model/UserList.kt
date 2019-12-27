package com.sakharu.queregardercesoir.data.locale.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "userList")
data class UserList(

    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    var id:Long?,

    @field:SerializedName("name")
    var name:String="",

    @field:SerializedName("imageResourceName")
    var iconResourceName:String=""

) : Serializable