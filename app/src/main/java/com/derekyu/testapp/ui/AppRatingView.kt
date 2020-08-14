package com.derekyu.testapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.derekyu.testapp.R
import kotlinx.android.synthetic.main.view_app_rating.view.*
import kotlin.math.roundToInt

class AppRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_app_rating, this)
    }

    @SuppressLint("SetTextI18n")
    fun setRating(rating: Float, ratingCount: Int) {
        val ratingInt = rating.roundToInt()
        val starOn = ContextCompat.getDrawable(context, android.R.drawable.star_on)
        val starOff = ContextCompat.getDrawable(context, android.R.drawable.star_off)
        if (ratingInt > 0) {
            star1.setImageDrawable(starOn)
        } else {
            star1.setImageDrawable(starOff)
        }
        if (ratingInt > 1) {
            star2.setImageDrawable(starOn)
        } else {
            star2.setImageDrawable(starOff)
        }
        if (ratingInt > 2) {
            star3.setImageDrawable(starOn)
        } else {
            star3.setImageDrawable(starOff)
        }
        if (ratingInt > 3) {
            star4.setImageDrawable(starOn)
        } else {
            star4.setImageDrawable(starOff)
        }
        if (ratingInt > 4) {
            star5.setImageDrawable(starOn)
        } else {
            star5.setImageDrawable(starOff)
        }
        rating_count.text = "(${ratingCount.toString()})"
    }
}