package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppCategoryAttributes(
    @SerializedName("term")
    val term: String,
    @SerializedName("label")
    val label: String
)