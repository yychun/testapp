package com.derekyu.testapp.data.model

import com.google.gson.annotations.SerializedName

data class AppCategory(
    @SerializedName("attributes")
    val attributes: AppCategoryAttributes
)