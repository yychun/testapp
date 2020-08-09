package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppName(
    @SerializedName("label")
    val label: String
)