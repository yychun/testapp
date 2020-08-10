package com.derekyu.testapp.data.model

import android.content.Context
import com.derekyu.testapp.R

sealed class MyError(
    val throwable: Throwable
) {
    abstract fun errorMsg(context: Context): String

    class Network(throwable: Throwable) : MyError(throwable) {
        override fun errorMsg(context: Context): String =
            context.getString(R.string.error_msg_network)
    }

    class Unknown(throwable: Throwable) : MyError(throwable) {
        override fun errorMsg(context: Context): String = throwable.localizedMessage ?: ""
    }
}