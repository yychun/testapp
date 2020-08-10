package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppInfo(
    @SerializedName("im:artist")
    val appArtist: AppArtist,
    @SerializedName("id")
    val appID: AppID,
    @SerializedName("category")
    val category: AppCategory,
    @SerializedName("im:image")
    val image: List<AppImage>,
    @SerializedName("im:name")
    val name: AppName,
    @SerializedName("summary")
    val summary: AppSummary
)