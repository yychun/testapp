package com.derekyu.testapp.utils

import com.derekyu.testapp.data.model.MyError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.mapToMyError() = when (this) {
    is IOException, is HttpException -> MyError.Network(this)
    else -> MyError.Unknown(this)
}