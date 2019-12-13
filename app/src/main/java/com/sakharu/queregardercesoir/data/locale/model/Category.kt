package com.sakharu.queregardercesoir.data.locale.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*
    Categorie représente les catégories que l'on peut afficher sur la home comme par
    exemple les films populaires du moment, les films les mieux notés, etc...
 */
@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    var id:Long?,
    @field:SerializedName("name")
    var name:String="unknown"
)