package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppInfo(
    @SerializedName("id")
    val appID: AppID,
    @SerializedName("im:name")
    val name: AppName,
    @SerializedName("category")
    val category: AppCategory,
    @SerializedName("im:image")
    val image: List<AppImage>
)