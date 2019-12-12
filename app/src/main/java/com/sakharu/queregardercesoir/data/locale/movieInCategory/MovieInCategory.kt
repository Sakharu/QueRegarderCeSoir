package com.sakharu.queregardercesoir.data.locale.movieInCategory

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/*
    MovieInCategory représente la liaison entre la table categorie et la table movie
    Cette table permet de garder en base les films inscrit dans une certaine categorie
    (comme par exemple les films populaires)
 */
@Entity(tableName = "movieInCategory")
data class MovieInCategory(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    var id:Long?,
    @field:SerializedName("idCategory")
    var idCategory:Long,
    @field:SerializedName("idMovie")
    var idMovie:Long
)