package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppArtist(
    @SerializedName("label")
    val label: String,
    @SerializedName("attributes")
    val attributes: AppArtistAttributes
)