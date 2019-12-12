package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class ResponseAuth(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("expires_at")
    val expires_at: String="",
    @SerializedName("request_token")
    val request_token: String=""

)