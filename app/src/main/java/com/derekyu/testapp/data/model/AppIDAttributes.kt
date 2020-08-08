package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppIDAttributes(
    @SerializedName("im:id")
    val id: String
)