package com.derekyu.testapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.derekyu.testapp.R
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.MyLoadState
import kotlinx.android.synthetic.main.view_app_recommendation.view.*

class AppRecommendationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private val adapter: AppRecommendationAdapter
    var onRetryCallback: (() -> Unit)? = null
    private lateinit var state: MyLoadState<List<AppInfoDTO>>

    init {
        LayoutInflater.from(context).inflate(R.layout.view_app_recommendation, this)

        adapter = AppRecommendationAdapter()
        app_recommendation_recycler_view.adapter = adapter

        app_recommendation_state_view.onRetryCallback = onRetryCallback
    }

    fun submitData(list: List<AppInfoDTO>) {
        adapter.submitData(list)
    }

    @SuppressLint("SetTextI18n")
    fun setState(state: MyLoadState<List<AppInfoDTO>>) {
        this.state = state
        val isEmptyData = when (state) {
            is MyLoadState.Success -> state.data.isEmpty()
            is MyLoadState.Fail -> true
        }
        (app_recommendation_state_view as MyStateView<List<AppInfoDTO>>).apply {
            dataView = app_recommendation_recycler_view
            setState(state, isEmptyData)
        }
    }
}