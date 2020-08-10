package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppSummary(
    @SerializedName("label")
    val label: String
)