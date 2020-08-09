package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppLookupResult(
    @SerializedName("artworkUrl100")
    val artworkUrl: String,
    @SerializedName("averageUserRating")
    val averageUserRating: Float,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String,
    @SerializedName("trackId")
    val trackId: String,
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("userRatingCount")
    val userRatingCount: Int
)