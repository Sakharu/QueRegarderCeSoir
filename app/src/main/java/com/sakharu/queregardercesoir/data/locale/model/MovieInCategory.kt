package com.sakharu.queregardercesoir.data.locale.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*
    MovieInCategory représente la liaison entre la table categorie et la table movie
    Cette table permet de garder en base les films inscrit dans une certaine categorie
    (comme par exemple les films populaires)

    les indices permettent de ne pas insérer plusieurs fois la même ligne, corrêlé avec le paramètre unique à true
 */
@Entity(tableName = "movieInCategory",indices = [Index(value = ["idCategory","idMovie"], unique = true)])
data class MovieInCategory(

    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    var id:Long?,

    @ForeignKey(entity = Category::class,parentColumns = ["idCategory"],childColumns = ["id"], onDelete = ForeignKey.CASCADE)
    @field:SerializedName("idCategory")
    var idCategory:Long,

    @ForeignKey(entity = Movie::class,parentColumns = ["idMovie"],childColumns = ["id"], onDelete = ForeignKey.CASCADE)
    @field:SerializedName("idMovie")
    var idMovie:Long
)