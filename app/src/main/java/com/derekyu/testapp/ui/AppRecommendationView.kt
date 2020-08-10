package com.derekyu.testapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.derekyu.testapp.R
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.MyError
import kotlinx.android.synthetic.main.view_app_recommendation.view.*

class AppRecommendationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private val adapter: AppRecommendationAdapter
    var retryCallback: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_app_recommendation, this)

        adapter = AppRecommendationAdapter()
        app_recommendation_recycler_view.adapter = adapter

        app_recommendation_button_retry.setOnClickListener {
            retryCallback?.invoke()
        }
    }

    fun submitData(list: List<AppInfoDTO>) {
        adapter.submitData(list)

        app_recommendation_recycler_view.visibility = View.VISIBLE
        app_recommendation_button_retry.visibility = View.INVISIBLE
    }

    @SuppressLint("SetTextI18n")
    fun showRetry(error: MyError) {
        app_recommendation_button_retry.text = "${context.getString(R.string.retry)} (${error.errorMsg(context)})"
        app_recommendation_button_retry.visibility = View.VISIBLE
        app_recommendation_recycler_view.visibility = View.INVISIBLE
    }
}