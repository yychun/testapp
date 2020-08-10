package com.derekyu.testapp.data.model

sealed class MyLoadState<T> {
    data class Success<T>(
        val data: T
    ) : MyLoadState<T>()

    data class Error<T>(
        val error: MyError
    ) : MyLoadState<T>()

    class Loading<T>: MyLoadState<T>()
}