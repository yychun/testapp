package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppID(
    @SerializedName("attributes")
    val attributes: AppIDAttributes
)