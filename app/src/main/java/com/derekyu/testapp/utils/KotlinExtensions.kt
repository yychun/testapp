package com.derekyu.testapp.utils

import android.content.Context
import android.util.TypedValue


fun Number.spToPx(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun Number.pxToSp(context: Context) =
    this.toFloat() / context.resources.displayMetrics.scaledDensity

fun Number.dpToPx(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun Number.pxToDp(context: Context) =
    this.toFloat() / context.resources.displayMetrics.density