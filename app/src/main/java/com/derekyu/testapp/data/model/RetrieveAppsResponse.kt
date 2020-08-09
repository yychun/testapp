package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class RetrieveAppsResponse(
    @SerializedName("feed")
    val feed: AppInfoFeed
)