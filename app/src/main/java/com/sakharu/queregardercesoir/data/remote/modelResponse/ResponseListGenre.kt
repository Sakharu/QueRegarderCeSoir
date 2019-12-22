package com.sakharu.queregardercesoir.data.remote.modelResponse

import com.google.gson.annotations.SerializedName
import com.sakharu.queregardercesoir.data.locale.model.Genre

data class ResponseListGenre(

    @SerializedName("genres")
    val genres: List<Genre>

)