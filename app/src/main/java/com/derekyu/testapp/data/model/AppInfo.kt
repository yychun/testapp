package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppInfo(
    @SerializedName("im:id")
    val appID: AppID
)